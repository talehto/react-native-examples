package com.ttsalarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "Alarm received, starting TTS service");
        String message = intent.getStringExtra("alarm_message");
        Intent serviceIntent = new Intent(context, TTSService.class);
        serviceIntent.putExtra("alarm_message", message);
        context.startForegroundService(serviceIntent);
    }
}
