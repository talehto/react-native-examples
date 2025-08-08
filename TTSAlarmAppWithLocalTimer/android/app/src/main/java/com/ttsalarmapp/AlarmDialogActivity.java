package com.ttsalarmapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class AlarmDialogActivity extends Activity {

    private BroadcastReceiver alarmFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish(); // Closing a dialog.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String message = getIntent().getStringExtra("alarm_message");

        new AlertDialog.Builder(this)
                .setTitle("Hälytys")
                .setMessage("Hälytysviesti: " + message)
                .setCancelable(false)
                .setPositiveButton("Pysäytä", (dialog, which) -> {
                    stopService(new Intent(this, TTSService.class));
                    finish();
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(alarmFinishedReceiver, new IntentFilter("com.ttsalarmapp.ACTION_ALARM_FINISHED"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(alarmFinishedReceiver);
    }
}

