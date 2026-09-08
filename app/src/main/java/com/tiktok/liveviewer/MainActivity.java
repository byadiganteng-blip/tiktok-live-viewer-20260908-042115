package com.tiktok.liveviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    private EditText etUsername, etServerUrl;
    private Button btnStart;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_main);
            
            etUsername = findViewById(R.id.etUsername);
            etServerUrl = findViewById(R.id.etServerUrl);
            btnStart = findViewById(R.id.btnStart);
            
            if (etServerUrl != null) {
                etServerUrl.setText("ws://YOUR_SERVER:8765");
            }
            
            if (btnStart != null) {
                btnStart.setOnClickListener(v -> startFloatingWindow());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    
    private void startFloatingWindow() {
        try {
            String username = etUsername != null ? etUsername.getText().toString().trim() : "";
            
            if (username.isEmpty()) {
                Toast.makeText(this, "Masukkan username TikTok", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Check overlay permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                    Toast.makeText(this, "Izinkan overlay dulu", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            
            Intent serviceIntent = new Intent(this, FloatingService.class);
            serviceIntent.putExtra("username", username);
            serviceIntent.putExtra("server_url", 
                etServerUrl != null ? etServerUrl.getText().toString().trim() : "");
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
            
            Toast.makeText(this, "Floating window started!", Toast.LENGTH_SHORT).show();
            
        } catch (Exception e) {
            Toast.makeText(this, "Gagal start: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}