
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etServerUrl = findViewById(R.id.etServerUrl);
        Button btnStart = findViewById(R.id.btnStart);
        
        btnStart.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(this, "Masukkan username", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                return;
            }
            
            Intent serviceIntent = new Intent(this, FloatingService.class);
            serviceIntent.putExtra("username", username);
            serviceIntent.putExtra("server_url", etServerUrl.getText().toString().trim());
            startService(serviceIntent);
            
            Toast.makeText(this, "Floating started!", Toast.LENGTH_SHORT).show();
        });
    }
}
