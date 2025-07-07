// StudentSubmitAssignmentsActivity.java
package com.example.tutionmanagementappication;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StudentSubmitAssignmentsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Submit Assignments (Coming Soon)");
        tv.setTextSize(22);
        setContentView(tv);
    }
}
