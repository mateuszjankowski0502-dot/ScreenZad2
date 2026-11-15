
package com.example.rejestracjawarsztaty;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {

    private static final String TAG = "CykleAktywnosci";

    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_EMAIL = "EXTRA_EMAIL";
    public static final String EXTRA_AGE = "EXTRA_AGE";
    public static final String EXTRA_WORKSHOP = "EXTRA_WORKSHOP";
    public static final String EXTRA_EXPERIENCE = "EXTRA_EXPERIENCE";

    private String summaryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SummaryActivity: onCreate");
        setContentView(R.layout.activity_summary);

        TextView tvSummary = findViewById(R.id.tvSummary);
        Button btnConfirm = findViewById(R.id.btnConfirm);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnShare = findViewById(R.id.btnShare);

        Intent intent = getIntent();

        String name = intent.getStringExtra(EXTRA_NAME);
        String email = intent.getStringExtra(EXTRA_EMAIL);
        int age = intent.getIntExtra(EXTRA_AGE, 0);
        String workshop = intent.getStringExtra(EXTRA_WORKSHOP);
        boolean experience =
                intent.getBooleanExtra(EXTRA_EXPERIENCE, false);

        String experienceText = getString(
                experience ? R.string.yes : R.string.no
        );

        summaryText = getString(
                R.string.summary_format,
                name,
                email,
                age,
                workshop,
                experienceText
        );

        tvSummary.setText(summaryText);

        btnConfirm.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        btnShare.setOnClickListener(v -> shareSummary());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "SummaryActivity: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "SummaryActivity: onDestroy");
    }

    private void shareSummary() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, summaryText);

        try {
            Intent chooser = Intent.createChooser(
                    shareIntent,
                    getString(R.string.share_title)
            );

            startActivity(chooser);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(
                    this,
                    R.string.share_error,
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}
