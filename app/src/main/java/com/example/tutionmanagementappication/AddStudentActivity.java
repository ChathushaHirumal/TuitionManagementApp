// ------------------ AddStudentActivity.java ------------------
package com.example.tutionmanagementappication;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudentActivity extends AppCompatActivity {

    EditText etStudentId, etPassword;
    Button btnSave;
    DatabaseReference usersRef, studentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        etStudentId = findViewById(R.id.etStudentId);
        etPassword = findViewById(R.id.etPassword);
        btnSave = findViewById(R.id.btnSave);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        studentsRef = FirebaseDatabase.getInstance().getReference("students");

        btnSave.setOnClickListener(v -> addStudent());
    }

    private void addStudent() {
        String id = etStudentId.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Enter both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        usersRef.child(id).child("username").setValue(id);
        usersRef.child(id).child("password").setValue(pass);
        usersRef.child(id).child("role").setValue("student");

        studentsRef.child(id).child("name").setValue("Student Name"); // optional

        Toast.makeText(this, "Student added!", Toast.LENGTH_SHORT).show();
        etStudentId.setText("");
        etPassword.setText("");
    }
}
