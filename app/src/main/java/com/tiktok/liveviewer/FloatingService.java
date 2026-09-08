
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

public class FloatingService extends Service {
    private WindowManager windowManager;
    private View floatingView;
    private TextView tvComments;
    private StringBuilder comments = new StringBuilder();
    
    @Override
    public IBinder onBind(Intent intent) { return null; }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotification();
        setupFloating();
        return START_STICKY;
    }
    
    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                "live", "Live", NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        Notification n = new Notification.Builder(this, "live")
            .setContentTitle("TikTok Live")
            .setSmallIcon(android.R.drawable.ic_menu_view)
            .build();
        startForeground(1, n);
    }
    
    private void setupFloating() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_live, null);
        tvComments = floatingView.findViewById(R.id.tvComments);
        tvComments.setText("Connected...");
        
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            300, 300,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.END;
        params.x = 10; params.y = 100;
        windowManager.addView(floatingView, params);
    }
    
    @Override
    public void onDestroy() {
        if (floatingView != null) windowManager.removeView(floatingView);
        super.onDestroy();
    }
}
