package com.example.main_activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import java.util.concurrent.Executor;

public class Fingerprint_Page extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_page); // Updated to correct layout file

        // Initialize executor
        executor = ContextCompat.getMainExecutor(this);

        // Initialize BiometricPrompt
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // User clicked the "Cancel" button, exit the app
                    finish();
                } else if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                    // No biometrics enrolled, redirect to settings
                    AlertDialog.Builder builder = new AlertDialog.Builder(Fingerprint_Page.this)
                            .setTitle("No Fingerprint Setup")
                            .setMessage("No fingerprint enrolled. You will be redirected to settings to set up fingerprint authentication.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                Intent intent = new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS);
                                startActivity(intent);
                            })
                            .setCancelable(false); // Make dialog non-cancelable

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    showToast("Authentication error: " + errString);
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                showToast("Authentication failed");
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent successfingerprint = new Intent(Fingerprint_Page.this,Login_Page.class);
                startActivity(successfingerprint);
            }
        });

        // Initialize prompt info
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Log in using your fingerprint")
                .setNegativeButtonText("Cancel")
                .build();

        authenticateWithFingerprint();
    }

    private void authenticateWithFingerprint() {
        biometricPrompt.authenticate(promptInfo);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}