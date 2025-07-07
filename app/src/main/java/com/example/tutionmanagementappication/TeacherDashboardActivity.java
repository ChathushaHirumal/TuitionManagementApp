package com.example.tutionmanagementappication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {

    Button btnTakeAttendance, btnUploadAssignments, btnUploadResults, btnManageMaterials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        btnTakeAttendance = findViewById(R.id.btnTakeAttendance);
        btnUploadAssignments = findViewById(R.id.btnUploadAssignments);
        btnUploadResults = findViewById(R.id.btnUploadResults);
        btnManageMaterials = findViewById(R.id.btnManageMaterials);

        btnTakeAttendance.setOnClickListener(v ->
                startActivity(new Intent(this, com.example.tutionmanagementappication.TakeAttendanceActivity.class)));

        btnUploadAssignments.setOnClickListener(v ->
                startActivity(new Intent(this, UploadAssignmentsActivity.class)));

        btnUploadResults.setOnClickListener(v ->
                startActivity(new Intent(this, UploadResultsActivity.class)));

        btnManageMaterials.setOnClickListener(v ->
                startActivity(new Intent(this, ManageMaterialsActivity.class)));
    }
}
