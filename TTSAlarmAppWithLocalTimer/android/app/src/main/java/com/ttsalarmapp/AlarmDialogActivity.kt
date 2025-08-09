package com.ttsalarmapp

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle

class AlarmDialogActivity : Activity() {
    private val alarmFinishedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            finish() // Closing a dialog.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Get message with null safety
        val message = getIntent().getStringExtra("alarm_message")
        val finalMessage = message ?: "Hälytysviesti"
        
        AlertDialog.Builder(this)
                .setTitle("Hälytys")
                .setMessage("Hälytysviesti: $finalMessage")
                .setCancelable(false)
                .setPositiveButton("Pysäytä") { dialog, which ->
                    stopService(Intent(this, TTSService::class.java))
                    finish()
                }
                .show()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(alarmFinishedReceiver, IntentFilter("com.ttsalarmapp.ACTION_ALARM_FINISHED"))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(alarmFinishedReceiver)
    }
}
