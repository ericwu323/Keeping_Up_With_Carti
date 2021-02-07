package com.example.keepingupwithcarti;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;


import com.example.keepingupwithcarti.Util.Database;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CartiAlarm extends BroadcastReceiver{
    public void onReceive(Context context, Intent intent){
        MediaPlayer mp = MediaPlayer.create(context, R.raw.slatt_sound);
        mp.start();

        Log.d("Alarm", "Alarm");
    }
}
