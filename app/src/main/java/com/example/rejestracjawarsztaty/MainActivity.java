
package com.example.rejestracjawarsztaty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CykleAktywnosci";

    private EditText etName;
    private EditText etEmail;
    private EditText etAge;
    private Spinner spWorkshop;
    private CheckBox cbExperience;
    private Button btnNext;
    private Button btnAbout;

    private final ActivityResultLauncher<Intent> summaryLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Toast.makeText(
                                    this,
                                    R.string.success_message,
                                    Toast.LENGTH_LONG
                            ).show();
                        } else if (result.getResultCode() == RESULT_CANCELED) {
                            Toast.makeText(
                                    this,
                                    R.string.cancel_message,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MainActivity: onCreate");
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etAge = findViewById(R.id.etAge);
        spWorkshop = findViewById(R.id.spWorkshop);
        cbExperience = findViewById(R.id.cbExperience);
        btnNext = findViewById(R.id.btnNext);
        btnAbout = findViewById(R.id.btnAbout);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.workshops,
                        android.R.layout.simple_spinner_item
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spWorkshop.setAdapter(adapter);

        btnNext.setOnClickListener(v -> validateForm());

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity: onDestroy");
    }

    private void validateForm() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String ageText = etAge.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError(getString(R.string.error_name));
            etName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.error_email));
            etEmail.requestFocus();
            return;
        }

        if (ageText.isEmpty()) {
            etAge.setError(getString(R.string.error_age));
            etAge.requestFocus();
            return;
        }

        int age;

        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            etAge.setError(getString(R.string.error_age));
            etAge.requestFocus();
            return;
        }

        if (age < 13 || age > 100) {
            etAge.setError(getString(R.string.error_age));
            etAge.requestFocus();
            return;
        }

        String workshop = spWorkshop.getSelectedItem().toString();
        boolean experience = cbExperience.isChecked();

        Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
        intent.putExtra(SummaryActivity.EXTRA_NAME, name);
        intent.putExtra(SummaryActivity.EXTRA_EMAIL, email);
        intent.putExtra(SummaryActivity.EXTRA_AGE, age);
        intent.putExtra(SummaryActivity.EXTRA_WORKSHOP, workshop);
        intent.putExtra(SummaryActivity.EXTRA_EXPERIENCE, experience);

        summaryLauncher.launch(intent);
    }
}
