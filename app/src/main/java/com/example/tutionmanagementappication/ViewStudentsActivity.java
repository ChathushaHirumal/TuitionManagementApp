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

public class ViewStudentsActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> studentList;
    ArrayList<String> studentIds;
    ArrayAdapter<String> adapter;
    DatabaseReference studentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        listView = findViewById(R.id.listViewStudents);
        studentList = new ArrayList<>();
        studentIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        listView.setAdapter(adapter);

        studentsRef = FirebaseDatabase.getInstance().getReference("students");

        loadStudents();

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedUserId = studentIds.get(position);
            Intent intent = new Intent(this, UpdateStudentActivity.class);
            intent.putExtra("userId", selectedUserId);
            startActivity(intent);
        });
    }

    private void loadStudents() {
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                studentIds.clear();
                for (DataSnapshot studentSnap : snapshot.getChildren()) {
                    String firstName = studentSnap.child("firstName").getValue(String.class);
                    String lastName = studentSnap.child("lastName").getValue(String.class);
                    String grade = studentSnap.child("grade").getValue(String.class);

                    String fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
                    studentList.add("Name: " + fullName.trim() + "\nGrade: " + (grade != null ? grade : "N/A"));
                    studentIds.add(studentSnap.getKey());
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
