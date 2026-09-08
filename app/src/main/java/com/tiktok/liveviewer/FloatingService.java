package com.tiktok.liveviewer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingService extends Service {
    
    private WindowManager windowManager;
    private View floatingView;
    private TextView tvTitle, tvComments;
    
    @Override
    public IBinder onBind(Intent intent) { return null; }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            createNotification();
            setupFloatingWindow();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            stopSelf();
        }
        return START_STICKY;
    }
    
    private void createNotification() {
        try {
            String channelId = "live_channel";
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                    channelId, "Live Service", NotificationManager.IMPORTANCE_LOW);
                NotificationManager manager = getSystemService(NotificationManager.class);
                if (manager != null) {
                    manager.createNotificationChannel(channel);
                }
            }
            
            Notification.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = new Notification.Builder(this, channelId);
            } else {
                builder = new Notification.Builder(this);
            }
            
            Notification notification = builder
                .setContentTitle("TikTok Live")
                .setContentText("Floating window aktif")
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .build();
            
            startForeground(1, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupFloatingWindow() {
        try {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            floatingView = inflater.inflate(R.layout.floating_live, null);
            
            tvTitle = floatingView.findViewById(R.id.tvTitle);
            tvComments = floatingView.findViewById(R.id.tvComments);
            
            if (tvTitle != null) tvTitle.setText("TikTok Live");
            if (tvComments != null) tvComments.setText("Connected...");
            
            int layoutType;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutType = WindowManager.LayoutParams.TYPE_PHONE;
            }
            
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                300, 300,
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            );
            
            params.gravity = Gravity.TOP | Gravity.END;
            params.x = 10;
            params.y = 100;
            
            windowManager.addView(floatingView, params);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    @Override
    public void onDestroy() {
        try {
            if (floatingView != null && windowManager != null) {
                windowManager.removeView(floatingView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}