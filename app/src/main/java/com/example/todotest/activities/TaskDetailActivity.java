package com.example.todotest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todotest.R;
import com.example.todotest.database.Task;

public class TaskDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Task task = (Task) getIntent().getSerializableExtra("task");
        if (task != null) {
            TextView title = findViewById(R.id.taskTitle);
            TextView description = findViewById(R.id.taskDescription);
            TextView dueDate = findViewById(R.id.taskDueDate);
            Button editButton = findViewById(R.id.editButton);

            title.setText(task.getTitle());
            description.setText(task.getDescription());
            dueDate.setText(task.getFormattedDate());

            editButton.setOnClickListener(v -> {
                Intent editIntent = new Intent(TaskDetailActivity.this, AddEditTaskActivity.class);
                editIntent.putExtra("task_id", task.getId());
                startActivity(editIntent);
                finish();
            });
        }
    }
}