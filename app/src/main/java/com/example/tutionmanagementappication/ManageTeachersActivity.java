package com.example.tutionmanagementappication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManageTeachersActivity extends AppCompatActivity {

    Button btnAddTeacher, btnViewTeachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_teachers);

        btnAddTeacher = findViewById(R.id.btnAddTeacher);
        btnViewTeachers = findViewById(R.id.btnViewTeachers);

        btnAddTeacher.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTeacherActivity.class));
        });

        btnViewTeachers.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewTeachersActivity.class));
        });
    }
}
