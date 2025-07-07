package com.example.tutionmanagementappication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManageStudentsActivity extends AppCompatActivity {

    Button btnAddTeacher, btnViewTeachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        btnAddTeacher = findViewById(R.id.btnAddStudent);
        btnViewTeachers = findViewById(R.id.btnViewStudents);

        btnAddTeacher.setOnClickListener(v -> {
            startActivity(new Intent(this, AddStudentActivity.class));
        });

        btnViewTeachers.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewStudentsActivity.class));
        });
    }
}
