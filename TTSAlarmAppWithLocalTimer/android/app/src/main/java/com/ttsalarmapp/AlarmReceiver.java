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

        if (message == null || message.trim().isEmpty()) {
            message = "Hyvää huomenta! Tämä on herätyksesi.";
        }

        // Starting a tts service.
        Intent serviceIntent = new Intent(context, TTSService.class);
        serviceIntent.putExtra("alarm_message", message);
        context.startForegroundService(serviceIntent);

        // Starting a stop dialog.
        Intent dialogIntent = new Intent(context, AlarmDialogActivity.class);
        dialogIntent.putExtra("alarm_message", message);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(dialogIntent);
    }
}
