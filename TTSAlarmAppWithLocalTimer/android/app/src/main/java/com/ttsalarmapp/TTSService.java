package com.ttsalarmapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

public class TTSService extends Service {
    private TextToSpeech tts;
    private static final String CHANNEL_ID = "tts_channel";
    private String alarmMessage;

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(
                "tts_channel", "TTS Channel", NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("TTS Alarm")
                .setContentText("Speaking message...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

            startForeground(1, notification);
        }

        /*tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {

                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        Log.d("TTSService", "TTS started");
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        Log.d("TTSService", "TTS done, stopping service");
                        stopSelf(); // ✅ Service is stopped when speech has been done.
                    }

                    @Override
                    public void onError(String utteranceId) {
                        Log.e("TTSService", "TTS error");
                        stopSelf(); // Service is stopped also in the error situation.
                    }
                });

                String message = getIntentMessage();
                if (message == null || message.trim().isEmpty()) {
                    message = "Hyvää huomenta! Tämä on herätyksesi."; // Oletusteksti
                }

                tts.setLanguage(new Locale("fi"));
                tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "ALARM_UTTERANCE");
            }
        });
        */
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 🔹 Haetaan viesti Intentistä täällä
        if (intent != null) {
            alarmMessage = intent.getStringExtra("alarm_message");
        }
        if (alarmMessage == null || alarmMessage.trim().isEmpty()) {
            alarmMessage = "Hyvää huomenta! Tämä on herätyksesi.";
        }

        // Käynnistetään TTS täällä
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.getDefault());
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {}

                    @Override
                    public void onDone(String utteranceId) {
                        stopSelf();
                    }

                    @Override
                    public void onError(String utteranceId) {
                        stopSelf();
                    }
                });

                tts.setLanguage(new Locale("fi"));
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
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
