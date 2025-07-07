// ------------------ AddTeacherActivity.java ------------------
package com.example.tutionmanagementappication;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTeacherActivity extends AppCompatActivity {

    EditText etTeacherId, etPassword;
    Button btnSave;
    DatabaseReference usersRef, teachersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        etTeacherId = findViewById(R.id.etTeacherId);
        etPassword = findViewById(R.id.etPassword);
        btnSave = findViewById(R.id.btnSave);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        teachersRef = FirebaseDatabase.getInstance().getReference("teachers");

        btnSave.setOnClickListener(v -> addTeacher());
    }

    private void addTeacher() {
        String id = etTeacherId.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Enter both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        usersRef.child(id).child("username").setValue(id);
        usersRef.child(id).child("password").setValue(pass);
        usersRef.child(id).child("role").setValue("teacher");

        teachersRef.child(id).child("name").setValue("Teacher Name"); // optional default

        Toast.makeText(this, "Teacher added!", Toast.LENGTH_SHORT).show();
        etTeacherId.setText("");
        etPassword.setText("");
    }
}
