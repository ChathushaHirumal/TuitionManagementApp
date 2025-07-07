package com.example.tutionmanagementappication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ManageMaterialsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Course Materials Page (Coming Soon)");
        tv.setTextSize(22);
        setContentView(tv);
    }
}
