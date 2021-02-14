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
    int count = 0;

    public void onReceive(Context context, Intent intent){
        Toast.makeText(context, " :)*notification !", Toast.LENGTH_LONG).show();
        final MediaPlayer mp = MediaPlayer.create(context, R.raw.slatt_sound);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            int maxCount = 5;

            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(count < maxCount) {
                    count++;
                    mp.seekTo(0);
                    mp.start();
                }
            }});
        mp.start();
        ; // initialise outside listener to prevent looping




        Log.d("Alarm", "Alarm");
    }
}
