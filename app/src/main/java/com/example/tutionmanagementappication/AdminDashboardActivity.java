package com.example.tutionmanagementappication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    Button btnManageUsers, btnAssignStudents, btnViewAttendance, btnViewResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnAssignStudents = findViewById(R.id.btnAssignStudents);
        btnViewAttendance = findViewById(R.id.btnViewAttendance);
        btnViewResults = findViewById(R.id.btnViewResults);

        btnManageUsers.setOnClickListener(v ->
                startActivity(new Intent(this, ManageUsersActivity.class)));

        btnAssignStudents.setOnClickListener(v ->
                startActivity(new Intent(this, AssignStudentsActivity.class)));

        btnViewAttendance.setOnClickListener(v ->
                startActivity(new Intent(this, AttendanceReportActivity.class)));

        btnViewResults.setOnClickListener(v ->
                startActivity(new Intent(this, ResultReportActivity.class)));
    }
}
