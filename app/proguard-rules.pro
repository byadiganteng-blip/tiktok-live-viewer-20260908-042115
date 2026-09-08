# Keep WebSocket
-keep class org.java_websocket.** { *; }

# Keep JSON
-keep class org.json.** { *; }

# Keep app classes
-keep class com.tiktok.liveviewer.** { *; }

# Remove logs
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}
