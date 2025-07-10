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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddStudentActivity extends AppCompatActivity {

    EditText etStudentId, etPassword, etFirstName, etLastName, etContact, etEmail, etAddress, etJoinedDate;
    Spinner spinnerGrade;
    Button btnSave;
    DatabaseReference usersRef, studentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // Initialize Views
        etStudentId = findViewById(R.id.etStudentId);
        etPassword = findViewById(R.id.etPassword);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        spinnerGrade = findViewById(R.id.spinnerGrade); // Changed from EditText to Spinner
        etContact = findViewById(R.id.etContact);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etJoinedDate = findViewById(R.id.etJoinedDate);
        btnSave = findViewById(R.id.btnSave);

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grade_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrade.setAdapter(adapter);

        // Firebase
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        studentsRef = FirebaseDatabase.getInstance().getReference("students");

        // Date picker
        etJoinedDate.setOnClickListener(v -> showDatePicker());

        // Save button
        btnSave.setOnClickListener(v -> addStudent());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, y, m, d) -> {
            String date = y + "-" + (m + 1) + "-" + d;
            etJoinedDate.setText(date);
        }, year, month, day).show();
    }

    private void addStudent() {
        String id = etStudentId.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String grade = spinnerGrade.getSelectedItem().toString(); // Get selected grade
        String contact = etContact.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String joinedDate = etJoinedDate.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(firstName) ||
                TextUtils.isEmpty(lastName) || TextUtils.isEmpty(contact) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(address) || TextUtils.isEmpty(joinedDate)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save login data
        usersRef.child(id).child("username").setValue(id);
        usersRef.child(id).child("password").setValue(pass);
        usersRef.child(id).child("role").setValue("student");

        // Save profile data
        DatabaseReference sRef = studentsRef.child(id);
        sRef.child("firstName").setValue(firstName);
        sRef.child("lastName").setValue(lastName);
        sRef.child("grade").setValue(grade);
        sRef.child("contact").setValue(contact);
        sRef.child("email").setValue(email);
        sRef.child("address").setValue(address);
        sRef.child("joinedDate").setValue(joinedDate);

        Toast.makeText(this, "Student added successfully!", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void clearFields() {
        etStudentId.setText("");
        etPassword.setText("");
        etFirstName.setText("");
        etLastName.setText("");
        spinnerGrade.setSelection(0); // Reset to first item
        etContact.setText("");
        etEmail.setText("");
        etAddress.setText("");
        etJoinedDate.setText("");
    }
}
