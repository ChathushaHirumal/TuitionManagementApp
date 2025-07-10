package com.example.tutionmanagementappication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;


public class TakeAttendanceActivity extends AppCompatActivity {

    TextView tvResult;
    Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_take_attendance);

        tvResult = findViewById(R.id.tvScanResult);
        btnScan = findViewById(R.id.btnScan);

        btnScan.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Scan a student QR code");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            //options.setCaptureActivity(CaptureActivityPortrait.class); // Portrait mode
            barcodeLauncher.launch(options);
        });
    }

    private final androidx.activity.result.ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    // Show scanned result (e.g., Student ID or JSON)
                    String studentId = result.getContents();
                    String currentDate = java.time.LocalDate.now().toString();

                    DatabaseReference dbRef = FirebaseDatabase.getInstance()
                            .getReference("attendance")
                            .child(studentId)
                            .child(currentDate);

                    dbRef.setValue("Present")
                            .addOnSuccessListener(unused ->
                                    tvResult.setText("✔ Attendance saved for " + studentId))
                            .addOnFailureListener(e ->
                                    tvResult.setText("❌ Failed: " + e.getMessage()));

                }
            });
}
