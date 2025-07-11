package com.example.tutionmanagementappication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentSubmitAssignmentsActivity extends AppCompatActivity {

    Spinner spinnerSubject;
    EditText etAssignmentContent, etDriveLink;
    Button btnSubmitAssignment;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_submit_assignments);

        String studentId = getIntent().getStringExtra("studentId");
        if (studentId == null || studentId.isEmpty()) {
            Toast.makeText(this, "Student ID missing. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Firebase setup
        dbRef = FirebaseDatabase.getInstance().getReference("assignmentsSubmitted");

        // Connect UI
        spinnerSubject = findViewById(R.id.spinnerSubject);
        etAssignmentContent = findViewById(R.id.etAssignmentContent);
        etDriveLink = findViewById(R.id.etDriveLink); // <- NEW: Google Drive Link field
        btnSubmitAssignment = findViewById(R.id.btnSubmitAssignment);

        // Dropdown
        String[] subjects = {"Mathematics", "English", "Science", "ICT"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, subjects);
        spinnerSubject.setAdapter(adapter);

        // Submit
        btnSubmitAssignment.setOnClickListener(v -> {
            String selectedSubject = spinnerSubject.getSelectedItem().toString();
            String content = etAssignmentContent.getText().toString().trim();
            String driveLink = etDriveLink.getText().toString().trim();

            if (content.isEmpty() && driveLink.isEmpty()) {
                Toast.makeText(this, "Please enter answer or a Drive link", Toast.LENGTH_SHORT).show();
                return;
            }

            String assignmentId = dbRef.child(studentId).push().getKey();
            if (assignmentId == null) {
                Toast.makeText(this, "Error generating assignment ID", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference assignmentRef = dbRef.child(studentId).child(assignmentId);

            assignmentRef.child("subject").setValue(selectedSubject);
            assignmentRef.child("content").setValue(content);
            assignmentRef.child("driveLink").setValue(driveLink);
            assignmentRef.child("timestamp").setValue(System.currentTimeMillis());

            Toast.makeText(this, "Assignment submitted!", Toast.LENGTH_SHORT).show();

            // Reset
            etAssignmentContent.setText("");
            etDriveLink.setText("");
            spinnerSubject.setSelection(0);
        });
    }
}
