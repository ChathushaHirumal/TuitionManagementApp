package com.example.tutionmanagementappication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;

public class AssignStudentsActivity extends AppCompatActivity {

    Spinner spinnerStudent, spinnerSubject, spinnerTeacher;
    EditText etStudentName, etGrade;
    Button btnSave;

    DatabaseReference studentsRef, teachersRef, assignRef;
    ArrayList< String> studentIds = new ArrayList<>();
    ArrayList<String> teacherIds = new ArrayList<>();
    ArrayAdapter<String> studentAdapter, subjectAdapter, teacherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_student);

        // UI Components
        spinnerStudent = findViewById(R.id.spinnerStudent);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        spinnerTeacher = findViewById(R.id.spinnerTeacher);
        etStudentName = findViewById(R.id.etStudentName);
        etGrade = findViewById(R.id.etGrade);
        btnSave = findViewById(R.id.btnSave);

        // Firebase Refs
        studentsRef = FirebaseDatabase.getInstance().getReference("students");
        teachersRef = FirebaseDatabase.getInstance().getReference("teachers");
        assignRef = FirebaseDatabase.getInstance().getReference("assignments");

        // Load dropdowns
        loadStudents();

        // Load subjects into spinner
        subjectAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.subject_array));
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(subjectAdapter);

        // Student dropdown selection
        spinnerStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String studentId = studentIds.get(position);
                studentsRef.child(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String firstName = snapshot.child("firstName").getValue(String.class);
                        String lastName = snapshot.child("lastName").getValue(String.class);
                        String grade = snapshot.child("grade").getValue(String.class);

                        etStudentName.setText(firstName + " " + lastName);
                        etGrade.setText(grade);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AssignStudentsActivity.this, "Failed to load student info", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Subject selection to load relevant teachers
        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = spinnerSubject.getSelectedItem().toString();
                loadTeachersForSubject(selectedSubject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Save button
        btnSave.setOnClickListener(v -> saveAssignment());
    }

    private void loadStudents() {
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> usernames = new ArrayList<>();
                studentIds.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    studentIds.add(snap.getKey());
                    usernames.add(snap.getKey()); // Username = ID
                }

                studentAdapter = new ArrayAdapter<>(AssignStudentsActivity.this,
                        android.R.layout.simple_spinner_item, usernames);
                studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerStudent.setAdapter(studentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AssignStudentsActivity.this, "Failed to load students", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTeachersForSubject(String subject) {
        teachersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> teacherNames = new ArrayList<>();
                teacherIds.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    String tSubject = snap.child("subject").getValue(String.class);
                    if (tSubject != null && tSubject.equals(subject)) {
                        String fullName = snap.child("fullName").getValue(String.class);
                        teacherNames.add(fullName);
                        teacherIds.add(snap.getKey());
                    }
                }

                teacherAdapter = new ArrayAdapter<>(AssignStudentsActivity.this,
                        android.R.layout.simple_spinner_item, teacherNames);
                teacherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTeacher.setAdapter(teacherAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AssignStudentsActivity.this, "Failed to load teachers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAssignment() {
        int studentIndex = spinnerStudent.getSelectedItemPosition();
        int teacherIndex = spinnerTeacher.getSelectedItemPosition();
        String subject = spinnerSubject.getSelectedItem().toString();

        if (studentIndex < 0 || teacherIndex < 0 || TextUtils.isEmpty(subject)) {
            Toast.makeText(this, "Please select all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String studentId = studentIds.get(studentIndex);
        String teacherId = teacherIds.get(teacherIndex);
        String studentName = etStudentName.getText().toString();
        String grade = etGrade.getText().toString();
        String teacherName = spinnerTeacher.getSelectedItem().toString();

        HashMap<String, Object> data = new HashMap<>();
        data.put("studentId", studentId);
        data.put("studentName", studentName);
        data.put("grade", grade);
        data.put("subject", subject);
        data.put("teacherId", teacherId);
        data.put("teacherName", teacherName);

        assignRef.child(studentId).child(subject).setValue(data)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Assignment saved!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save!", Toast.LENGTH_SHORT).show());
    }
}
