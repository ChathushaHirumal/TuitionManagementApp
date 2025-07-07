// ------------------ UpdateStudentActivity.java ------------------
package com.example.tutionmanagementappication;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class UpdateStudentActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Spinner spinnerRole;
    Button btnUpdate, btnDelete;
    DatabaseReference usersRef;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        userId = getIntent().getStringExtra("userId");
        loadUserData();

        btnUpdate.setOnClickListener(v -> updateUser());
        btnDelete.setOnClickListener(v -> deleteUser());
    }

    private void loadUserData() {
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateStudentActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        usersRef.child(userId).child("username").setValue(username);
        usersRef.child(userId).child("password").setValue(password);
        usersRef.child(userId).child("role").setValue(role);

        Toast.makeText(this, "Student updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteUser() {
        usersRef.child(userId).removeValue().addOnSuccessListener(unused -> {
            Toast.makeText(this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to delete student", Toast.LENGTH_SHORT).show();
        });
    }
}
