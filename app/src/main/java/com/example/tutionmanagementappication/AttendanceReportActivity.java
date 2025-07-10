package com.example.tutionmanagementappication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class AttendanceReportActivity extends AppCompatActivity {

    LinearLayout attendanceContainer;
    TextView tvPresentCount, tvAbsentCount, tvDate, tvClassName;
    Button btnSubmitAttendance;

    int presentCount = 0, absentCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);

        attendanceContainer = findViewById(R.id.attendanceContainer);
        tvPresentCount = findViewById(R.id.tvPresentCount);
        tvAbsentCount = findViewById(R.id.tvAbsentCount);
        tvDate = findViewById(R.id.tvDate);
        tvClassName = findViewById(R.id.tvClassName);
        btnSubmitAttendance = findViewById(R.id.btnSubmitAttendance);

        // Optional: Set today's date and sample class name
        tvDate.setText("📅 " + java.time.LocalDate.now().toString());
        tvClassName.setText("📘 Basic of Geometry Class");

        fetchAttendanceFromFirebase();
    }

    private void fetchAttendanceFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("attendance");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendanceContainer.removeAllViews();
                presentCount = 0;
                absentCount = 0;

                for (DataSnapshot studentSnap : snapshot.getChildren()) {
                    String studentId = studentSnap.getKey();

                    for (DataSnapshot dateSnap : studentSnap.getChildren()) {
                        String date = dateSnap.getKey();
                        String status = dateSnap.getValue(String.class);

                        // Count totals
                        if ("Present".equalsIgnoreCase(status)) {
                            presentCount++;
                        } else {
                            absentCount++;
                        }

                        addStudentRow(studentId, date, status);
                    }
                }

                tvPresentCount.setText("✔ " + presentCount + " Present");
                tvAbsentCount.setText("❌ " + absentCount + " Absent");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                TextView errorView = new TextView(AttendanceReportActivity.this);
                errorView.setText("Error loading attendance: " + error.getMessage());
                attendanceContainer.addView(errorView);
            }
        });
    }

    private void addStudentRow(String studentId, String date, String status) {
        // Card layout
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(24, 24, 24, 24);
        card.setBackgroundColor(Color.parseColor("#FFFFFF"));
        card.setElevation(8f);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 24);
        card.setLayoutParams(cardParams);

        // Student info
        TextView tvId = new TextView(this);
        tvId.setText("👤 " + studentId);
        tvId.setTextSize(16);
        tvId.setTextColor(Color.BLACK);
        tvId.setPadding(0, 0, 0, 8);

        // Date and status
        TextView tvDateStatus = new TextView(this);
        tvDateStatus.setText("📅 " + date + " — " + status);
        tvDateStatus.setTextSize(14);
        tvDateStatus.setTextColor(Color.DKGRAY);

        // Add to card
        card.addView(tvId);
        card.addView(tvDateStatus);

        // Add to container
        attendanceContainer.addView(card);
    }
}
