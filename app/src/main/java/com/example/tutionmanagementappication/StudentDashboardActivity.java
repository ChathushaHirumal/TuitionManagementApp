package com.example.tutionmanagementappication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

public class StudentDashboardActivity extends AppCompatActivity {

    TextView tvWelcomeName;  // dynamic welcome text
    String studentId;
    DatabaseReference usersRef;

    Button btnViewAttendance, btnViewAssignments, btnViewResults,
            btnSubmitAssignments, btnViewMaterials, btnNotifications, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        TextView tvDate = findViewById(R.id.tvDate);
        String currentDate = new java.text.SimpleDateFormat("MMMM d, yyyy", java.util.Locale.getDefault()).format(new java.util.Date());
        tvDate.setText(currentDate);


        // Get studentId passed from LoginActivity
        studentId = getIntent().getStringExtra("studentId");

        // Firebase reference
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Link views
        tvWelcomeName = findViewById(R.id.tvWelcomeName);
        btnBack = findViewById(R.id.btnBack);

        btnViewAttendance = findViewById(R.id.btnViewAttendance);
        btnViewAssignments = findViewById(R.id.btnViewAssignments);
        btnViewResults = findViewById(R.id.btnViewResults);
        btnSubmitAssignments = findViewById(R.id.btnSubmitAssignments);
        btnViewMaterials = findViewById(R.id.btnViewMaterials);
        btnNotifications = findViewById(R.id.btnNotifications);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Real-time name or ID fetch
        if (studentId != null) {
            usersRef.child(studentId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        if (name != null && !name.isEmpty()) {
                            tvWelcomeName.setText("Welcome, " + name + "!");
                        } else {
                            tvWelcomeName.setText("Welcome, " + studentId + "!");
                        }
                    } else {
                        tvWelcomeName.setText("Welcome, " + studentId + "!");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    tvWelcomeName.setText("Welcome, Student!");
                }
            });
        } else {
            tvWelcomeName.setText("Welcome, Student!");
        }

        // Button Navigation
        btnViewAttendance.setOnClickListener(v ->
                startActivity(new Intent(this, StudentViewAttendanceActivity.class)));

        btnViewAssignments.setOnClickListener(v ->
                startActivity(new Intent(this, StudentViewAssignmentsActivity.class)));

        btnViewResults.setOnClickListener(v ->
                startActivity(new Intent(this, StudentViewResultsActivity.class)));

        btnSubmitAssignments.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentSubmitAssignmentsActivity.class);
            intent.putExtra("studentId", studentId); // ✅ pass the studentId
            startActivity(intent);
        });


        btnViewMaterials.setOnClickListener(v ->
                startActivity(new Intent(this, StudentViewMaterialsActivity.class)));

        btnNotifications.setOnClickListener(v ->
                startActivity(new Intent(this, StudentNotificationsActivity.class)));
    }
}
