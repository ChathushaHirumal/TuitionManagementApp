package com.example.tutionmanagementappication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class StudentViewAttendanceActivity extends AppCompatActivity {

    String studentId = "S001"; // Later replace with login-based value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_attendance);

        TextView tvStudentId = findViewById(R.id.tvStudentId);
        LinearLayout container = findViewById(R.id.attendanceContainer);

        tvStudentId.setText("👤 Student ID: " + studentId);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("attendance")
                .child(studentId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    TextView msg = new TextView(StudentViewAttendanceActivity.this);
                    msg.setText("No attendance records found.");
                    msg.setTextColor(Color.GRAY);
                    msg.setTextSize(16f);
                    container.addView(msg);
                    return;
                }

                for (DataSnapshot dateSnap : snapshot.getChildren()) {
                    String date = dateSnap.getKey();
                    String status = dateSnap.getValue(String.class);

                    TextView record = new TextView(StudentViewAttendanceActivity.this);
                    record.setText("📆 " + date + "\n" + (status.equals("Present") ? "🟢" : "🔴") + " Status: " + status);
                    record.setTextSize(16f);
                    record.setTextColor(status.equals("Present") ? Color.parseColor("#2E7D32") : Color.parseColor("#C62828"));
                    record.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
                    record.setPadding(24, 24, 24, 24);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 0, 24);
                    record.setLayoutParams(params);

                    container.addView(record);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                TextView errorMsg = new TextView(StudentViewAttendanceActivity.this);
                errorMsg.setText("Failed to load: " + error.getMessage());
                errorMsg.setTextColor(Color.RED);
                errorMsg.setTextSize(16f);
                container.addView(errorMsg);
            }
        });
    }
}
