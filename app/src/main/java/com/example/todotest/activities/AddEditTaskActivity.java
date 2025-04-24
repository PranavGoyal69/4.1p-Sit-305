package com.example.todotest.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.todotest.R;
import com.example.todotest.database.Task;
import com.example.todotest.viewmodels.TaskViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditTaskActivity extends AppCompatActivity {
    private EditText titleEditText, descriptionEditText, dueDateEditText;
    private long dueDateInMillis;
    private TaskViewModel taskViewModel;
    private int taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        Button saveButton = findViewById(R.id.saveButton);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId != -1) {
            taskViewModel.getTaskById(taskId).observe(this, task -> {
                if (task != null) {
                    titleEditText.setText(task.getTitle());
                    descriptionEditText.setText(task.getDescription());
                    dueDateInMillis = task.getDueDate();
                    dueDateEditText.setText(task.getFormattedDate());
                }
            });
        }

        dueDateEditText.setOnClickListener(v -> showDatePicker());
        saveButton.setOnClickListener(v -> saveTask());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, day);
            dueDateInMillis = selectedDate.getTimeInMillis();
            dueDateEditText.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(selectedDate.getTime()));
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (title.isEmpty()) {
            titleEditText.setError("Title required");
            return;
        }

        if (dueDateInMillis == 0) {
            Toast.makeText(this, "Please select a due date", Toast.LENGTH_SHORT).show();
            return;
        }

        Task task = new Task(title, description, dueDateInMillis);
        if (taskId != -1) {
            task.setId(taskId);
            taskViewModel.update(task);
        } else {
            taskViewModel.insert(task);
        }
        finish();
    }
}