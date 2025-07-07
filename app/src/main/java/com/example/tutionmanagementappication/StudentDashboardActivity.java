package com.example.tutionmanagementappication;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboardActivity extends AppCompatActivity {

    Button btnViewAttendance, btnViewAssignments, btnViewResults, btnSubmitAssignments, btnViewMaterials, btnNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        btnViewAttendance = findViewById(R.id.btnViewAttendance);
        btnViewAssignments = findViewById(R.id.btnViewAssignments);
        btnViewResults = findViewById(R.id.btnViewResults);
        btnSubmitAssignments = findViewById(R.id.btnSubmitAssignments);
        btnViewMaterials = findViewById(R.id.btnViewMaterials);
        btnNotifications = findViewById(R.id.btnNotifications);

        btnViewAttendance.setOnClickListener(v ->
                startActivity(new Intent(this, StudentViewAttendanceActivity.class)));

        btnViewAssignments.setOnClickListener(v ->
                startActivity(new Intent(this, StudentViewAssignmentsActivity.class)));

        btnViewResults.setOnClickListener(v ->
                startActivity(new Intent(this, StudentViewResultsActivity.class)));

        btnSubmitAssignments.setOnClickListener(v ->
                startActivity(new Intent(this, StudentSubmitAssignmentsActivity.class)));

        btnViewMaterials.setOnClickListener(v ->
                startActivity(new Intent(this, StudentViewMaterialsActivity.class)));

        btnNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, StudentNotificationsActivity.class)));
    }
}
