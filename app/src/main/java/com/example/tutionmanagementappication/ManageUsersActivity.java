package com.example.tutionmanagementappication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManageUsersActivity extends AppCompatActivity {

    Button btnManageTeachers, btnManageStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        btnManageTeachers = findViewById(R.id.btnManageTeachers);
        btnManageStudents = findViewById(R.id.btnManageStudents);

        btnManageTeachers.setOnClickListener(v ->
                startActivity(new Intent(this, com.example.tutionmanagementappication.ManageTeachersActivity.class)));

        btnManageStudents.setOnClickListener(v ->
                startActivity(new Intent(this, ManageStudentsActivity.class)));
    }
}
