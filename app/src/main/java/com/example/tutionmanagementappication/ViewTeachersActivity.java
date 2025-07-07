// ------------------ ViewTeachersActivity.java ------------------
package com.example.tutionmanagementappication;

import android.content.Intent;
import android.os.Bundle;
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

public class ViewTeachersActivity extends AppCompatActivity {

    ListView listViewTeachers;
    ArrayList<String> teacherList;
    ArrayAdapter<String> adapter;
    DatabaseReference usersRef;
    ArrayList<String> teacherIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teachers);

        listViewTeachers = findViewById(R.id.listViewTeachers);
        teacherList = new ArrayList<>();
        teacherIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, teacherList);
        listViewTeachers.setAdapter(adapter);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        loadTeachers();

        listViewTeachers.setOnItemClickListener((parent, view, position, id) -> {
            String selectedId = teacherIds.get(position);
            Intent intent = new Intent(ViewTeachersActivity.this, UpdateTeacherActivity.class);
            intent.putExtra("userId", selectedId);
            startActivity(intent);
        });
    }

    private void loadTeachers() {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherList.clear();
                teacherIds.clear();
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    String role = userSnap.child("role").getValue(String.class);
                    if ("teacher".equals(role)) {
                        String username = userSnap.child("username").getValue(String.class);
                        String password = userSnap.child("password").getValue(String.class);
                        teacherList.add("Username: " + username + "\nPassword: " + password);
                        teacherIds.add(userSnap.getKey());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewTeachersActivity.this, "Failed to load teachers", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
