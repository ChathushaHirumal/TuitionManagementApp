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
    DatabaseReference teachersRef;
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

        teachersRef = FirebaseDatabase.getInstance().getReference("teachers");

        loadTeachers();

        listViewTeachers.setOnItemClickListener((parent, view, position, id) -> {
            String selectedId = teacherIds.get(position);
            Intent intent = new Intent(ViewTeachersActivity.this, UpdateTeacherActivity.class);
            intent.putExtra("userId", selectedId);
            startActivity(intent);
        });
    }

    private void loadTeachers() {
        teachersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherList.clear();
                teacherIds.clear();

                for (DataSnapshot teacherSnap : snapshot.getChildren()) {
                    String fullName = teacherSnap.child("fullName").getValue(String.class);
                    String subject = teacherSnap.child("subject").getValue(String.class);

                    teacherList.add("Name: " + fullName + "\nSubject: " + subject);
                    teacherIds.add(teacherSnap.getKey()); // Teacher ID (same as userId)
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
