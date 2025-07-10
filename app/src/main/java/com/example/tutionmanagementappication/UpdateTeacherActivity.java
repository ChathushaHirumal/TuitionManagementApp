package com.example.tutionmanagementappication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UpdateTeacherActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etFullName, etPhone, etEmail, etAddress, etJoinedDate;
    Spinner spinnerRole, spinnerSubject;
    Button btnUpdate, btnDelete;
    DatabaseReference usersRef, teachersRef;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        // Initialize UI
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etJoinedDate = findViewById(R.id.etJoinedDate);
        spinnerRole = findViewById(R.id.spinnerRole);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Set dropdowns
        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        ArrayAdapter<CharSequence> subjectAdapter = ArrayAdapter.createFromResource(this,
                R.array.subject_array, android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(subjectAdapter);

        // Firebase
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        teachersRef = FirebaseDatabase.getInstance().getReference("teachers");

        userId = getIntent().getStringExtra("userId");

        loadUserData();
        loadTeacherProfile();

        etJoinedDate.setOnClickListener(v -> showDatePicker());

        btnUpdate.setOnClickListener(v -> updateTeacher());
        btnDelete.setOnClickListener(v -> deleteTeacher());
    }

    private void loadUserData() {
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    etUsername.setText(snapshot.child("username").getValue(String.class));
                    etPassword.setText(snapshot.child("password").getValue(String.class));
                    String role = snapshot.child("role").getValue(String.class);
                    if (role != null) {
                        int position = ((ArrayAdapter) spinnerRole.getAdapter()).getPosition(role);
                        spinnerRole.setSelection(position);
                    }
                }
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateTeacherActivity.this, "Failed to load login info", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTeacherProfile() {
        teachersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    etFullName.setText(snapshot.child("fullName").getValue(String.class));
                    etPhone.setText(snapshot.child("phone").getValue(String.class));
                    etEmail.setText(snapshot.child("email").getValue(String.class));
                    etAddress.setText(snapshot.child("address").getValue(String.class));
                    etJoinedDate.setText(snapshot.child("joinedDate").getValue(String.class));

                    String subject = snapshot.child("subject").getValue(String.class);
                    if (subject != null) {
                        int position = ((ArrayAdapter) spinnerSubject.getAdapter()).getPosition(subject);
                        spinnerSubject.setSelection(position);
                    }
                }
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateTeacherActivity.this, "Failed to load teacher data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTeacher() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String subject = spinnerSubject.getSelectedItem().toString();
        String joinedDate = etJoinedDate.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(fullName) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(address) || TextUtils.isEmpty(joinedDate)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update user login
        usersRef.child(userId).child("username").setValue(username);
        usersRef.child(userId).child("password").setValue(password);
        usersRef.child(userId).child("role").setValue(role);

        // Update teacher profile
        DatabaseReference tRef = teachersRef.child(userId);
        tRef.child("fullName").setValue(fullName);
        tRef.child("phone").setValue(phone);
        tRef.child("email").setValue(email);
        tRef.child("address").setValue(address);
        tRef.child("subject").setValue(subject);
        tRef.child("joinedDate").setValue(joinedDate);

        Toast.makeText(this, "Teacher updated successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteTeacher() {
        usersRef.child(userId).removeValue();
        teachersRef.child(userId).removeValue();
        Toast.makeText(this, "Teacher deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            etJoinedDate.setText(date);
        }, year, month, day).show();
    }
}
