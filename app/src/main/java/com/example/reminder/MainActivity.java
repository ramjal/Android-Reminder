package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Timer timer;
    TimerTask timerTask;
    LocalDateTime reminderTime;
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();

    TextView txtCurrentTime;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy - hh:mm:ss a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reminderTime = LocalDateTime.now();
        txtCurrentTime = findViewById(R.id.txtCurrentTime);
        txtCurrentTime.setText(dtf.format(reminderTime));
    }

    @Override
    protected void onResume() {
        super.onResume();

        //onResume we start our timer so it can start when the app comes from the background
        startTimer();
    }

    //region UI Events Methods
    public void btnPlayClicked(View view) throws InterruptedException {
        reminderTime = reminderTime.plusMinutes(1);
        txtCurrentTime.setText(dtf.format(reminderTime));

        //final MediaPlayer myPlayer = MediaPlayer.create(this, R.raw.cat_sound);
        //myPlayer.start();
    }

    public void btnStopClicked(View view) {
        stopTimerTask();
    }
    //endregion

    //region Timer Methods
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, 5000); //
    }

    //stop the timer, if it's not already null
    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //use a handler to run a toast that shows the current timestamp on the original thread
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //get the current timeStamp
                        LocalDateTime curTime = LocalDateTime.now();
                        Toast.makeText(getApplicationContext(), dtf.format(LocalDateTime.now()), Toast.LENGTH_SHORT).show();
                        if (reminderTime.getMinute() <= curTime.getMinute()) {
                            txtCurrentTime.setText("Ha ha ha - Done!");
                            stopTimerTask();
                        }
                    }
                });
            }
        };
    }
    //endregion

}