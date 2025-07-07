// ------------------ ViewStudentsActivity.java ------------------
package com.example.tutionmanagementappication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewStudentsActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> studentList;
    ArrayList<String> studentIds;
    ArrayAdapter<String> adapter;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        listView = findViewById(R.id.listViewStudents);
        studentList = new ArrayList<>();
        studentIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        listView.setAdapter(adapter);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        loadStudents();

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedUserId = studentIds.get(position);
            Intent intent = new Intent(this, UpdateStudentActivity.class);
            intent.putExtra("userId", selectedUserId);
            startActivity(intent);
        });
    }

    private void loadStudents() {
        usersRef.orderByChild("role").equalTo("student")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        studentList.clear();
                        studentIds.clear();
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            String username = userSnapshot.child("username").getValue(String.class);
                            String password = userSnapshot.child("password").getValue(String.class);
                            if (username != null && password != null) {
                                studentList.add("Username: " + username + "\nPassword: " + password);
                                studentIds.add(userSnapshot.getKey());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ViewStudentsActivity.this, "Failed to load students", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
