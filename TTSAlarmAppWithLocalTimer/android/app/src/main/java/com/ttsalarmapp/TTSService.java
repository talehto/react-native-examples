package com.ttsalarmapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import androidx.core.app.NotificationCompat;

public class TTSService extends Service {
    private TextToSpeech tts;
    private static final String CHANNEL_ID = "tts_channel";
    private String alarmMessage;
    private int repeatCount = 0;
    private static final int MAX_REPEATS = 5;
    
    private BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopSelf();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Register broadcast receiver
        registerReceiver(stopReceiver, new IntentFilter("com.ttsalarmapp.STOP_TTS_SERVICE"));

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "TTS Channel", NotificationManager.IMPORTANCE_HIGH);
            
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            // 🔹 Intent for stop a tts service.
            Intent stopIntent = new Intent("com.ttsalarmapp.STOP_TTS_SERVICE");
            PendingIntent stopPendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    stopIntent,
                    PendingIntent.FLAG_IMMUTABLE
            );

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("TTS Alarm")
                .setContentText("Speaking message...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .addAction(new NotificationCompat.Action(
                        R.mipmap.ic_launcher,
                        "Pysäytä",
                        stopPendingIntent
                ))
                .build();

            startForeground(1, notification);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Retrieve message from the Intent object.
        if (intent != null) {
            alarmMessage = intent.getStringExtra("alarm_message");
        }

        // Starting the text-to-speech functionality. 
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("fi"));
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {}

                    @Override
                    public void onDone(String utteranceId) {
                        repeatCount++;
                        if (repeatCount < MAX_REPEATS) {
                            tts.speak(alarmMessage, TextToSpeech.QUEUE_FLUSH, null, "ALARM_UTTERANCE");
                        } else {
                            // Closing a "stop speech" dialog.
                            Intent doneIntent = new Intent("com.ttsalarmapp.ACTION_ALARM_FINISHED");
                            sendBroadcast(doneIntent);
                            stopSelf();
                        }
                    }

                    @Override
                    public void onError(String utteranceId) {
                        stopSelf();
                    }
                });

                tts.speak(alarmMessage, TextToSpeech.QUEUE_FLUSH, null, "ALARM_UTTERANCE");
            }
        });
        
        return START_STICKY;
        //return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        try {
            unregisterReceiver(stopReceiver);
        } catch (IllegalArgumentException e) {
            // Receiver was not registered
        }
        super.onDestroy();
    }

    public void stopSpeaking() {
        if (tts != null) {
            tts.stop();
        }
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
