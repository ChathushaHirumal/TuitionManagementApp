package com.example.tutionmanagementappication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AttendanceReportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Attendance Reports Page (Coming Soon)");
        tv.setTextSize(22);
        setContentView(tv);
    }
}
