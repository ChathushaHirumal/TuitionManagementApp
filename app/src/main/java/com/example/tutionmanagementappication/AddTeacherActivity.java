package com.example.tutionmanagementappication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddTeacherActivity extends AppCompatActivity {

    EditText etTeacherId, etPassword, etFullName, etPhone, etEmail, etAddress, etJoinedDate;
    Spinner spinnerSubject;
    Button btnSave;
    DatabaseReference usersRef, teachersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        // Initialize Views
        etTeacherId = findViewById(R.id.etTeacherId);
        etPassword = findViewById(R.id.etPassword);
        etFullName = findViewById(R.id.etFullName);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etJoinedDate = findViewById(R.id.etJoinedDate);
        btnSave = findViewById(R.id.btnSave);

        // Spinner values
        ArrayAdapter<CharSequence> subjectAdapter = ArrayAdapter.createFromResource(this,
                R.array.subject_array, android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(subjectAdapter);

        // Firebase refs
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        teachersRef = FirebaseDatabase.getInstance().getReference("teachers");

        // Date picker
        etJoinedDate.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> addTeacher());
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

    private void addTeacher() {
        String id = etTeacherId.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String name = etFullName.getText().toString().trim();
        String subject = spinnerSubject.getSelectedItem().toString();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String joined = etJoinedDate.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(subject) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(joined)) {

            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save login data
        usersRef.child(id).child("username").setValue(id);
        usersRef.child(id).child("password").setValue(pass);
        usersRef.child(id).child("role").setValue("teacher");

        // Save profile data
        DatabaseReference tRef = teachersRef.child(id);
        tRef.child("fullName").setValue(name);
        tRef.child("subject").setValue(subject);
        tRef.child("phone").setValue(phone);
        tRef.child("email").setValue(email);
        tRef.child("address").setValue(address);
        tRef.child("joinedDate").setValue(joined);

        Toast.makeText(this, "Teacher added successfully!", Toast.LENGTH_SHORT).show();

        clearFields();
    }

    private void clearFields() {
        etTeacherId.setText("");
        etPassword.setText("");
        etFullName.setText("");
        etPhone.setText("");
        etEmail.setText("");
        etAddress.setText("");
        etJoinedDate.setText("");
        spinnerSubject.setSelection(0);
    }
}
