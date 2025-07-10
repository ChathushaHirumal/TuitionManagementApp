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

public class UpdateStudentActivity extends AppCompatActivity {

    EditText etStudentId, etPassword, etFirstName, etLastName, etContact, etEmail, etAddress, etJoinedDate;
    Spinner spinnerGrade;
    Button btnUpdate, btnDelete;

    DatabaseReference usersRef, studentsRef;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);

        // Initialize views
        etStudentId = findViewById(R.id.etStudentId);
        etPassword = findViewById(R.id.etPassword);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etContact = findViewById(R.id.etContact);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etJoinedDate = findViewById(R.id.etJoinedDate);
        spinnerGrade = findViewById(R.id.spinnerGrade);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.grade_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrade.setAdapter(adapter);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        studentsRef = FirebaseDatabase.getInstance().getReference("students");

        // Get selected userId
        userId = getIntent().getStringExtra("userId");

        // Load existing data
        loadStudentData();

        etJoinedDate.setOnClickListener(v -> showDatePicker());
        btnUpdate.setOnClickListener(v -> updateStudent());
        btnDelete.setOnClickListener(v -> deleteStudent());
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

    private void loadStudentData() {
        // Load user credentials
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                etStudentId.setText(snapshot.child("username").getValue(String.class));
                etPassword.setText(snapshot.child("password").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateStudentActivity.this, "Failed to load login info", Toast.LENGTH_SHORT).show();
            }
        });

        // Load student profile
        studentsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                etFirstName.setText(snapshot.child("firstName").getValue(String.class));
                etLastName.setText(snapshot.child("lastName").getValue(String.class));
                etContact.setText(snapshot.child("contact").getValue(String.class));
                etEmail.setText(snapshot.child("email").getValue(String.class));
                etAddress.setText(snapshot.child("address").getValue(String.class));
                etJoinedDate.setText(snapshot.child("joinedDate").getValue(String.class));
                String grade = snapshot.child("grade").getValue(String.class);

                if (grade != null) {
                    ArrayAdapter adapter = (ArrayAdapter) spinnerGrade.getAdapter();
                    int pos = adapter.getPosition(grade);
                    spinnerGrade.setSelection(pos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateStudentActivity.this, "Failed to load student profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStudent() {
        String password = etPassword.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String grade = spinnerGrade.getSelectedItem().toString();
        String contact = etContact.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String joined = etJoinedDate.getText().toString().trim();

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)
                || TextUtils.isEmpty(contact) || TextUtils.isEmpty(email) || TextUtils.isEmpty(address)
                || TextUtils.isEmpty(joined)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        usersRef.child(userId).child("password").setValue(password);

        DatabaseReference sRef = studentsRef.child(userId);
        sRef.child("firstName").setValue(firstName);
        sRef.child("lastName").setValue(lastName);
        sRef.child("grade").setValue(grade);
        sRef.child("contact").setValue(contact);
        sRef.child("email").setValue(email);
        sRef.child("address").setValue(address);
        sRef.child("joinedDate").setValue(joined);

        Toast.makeText(this, "Student updated successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteStudent() {
        usersRef.child(userId).removeValue();
        studentsRef.child(userId).removeValue();
        Toast.makeText(this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
