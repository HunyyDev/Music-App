package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private int oneTime = 1;
    private SeekBar skbMusicPlayer;
    private double finalTime, startTime;
    private TextView tvTime, tvCurrentTime;
    private ImageButton ibFastForward, ibFastRewind, ibPlay, ibPause;
    MediaPlayer mediaPlayer;
    int forwardTime = 10000;
    int backwardTime = 10000;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiate();
    }

    private void initiate() {
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        skbMusicPlayer = findViewById(R.id.skbMusicPlayer);
        ibFastForward = findViewById(R.id.ibFastForward);
        ibFastRewind = findViewById(R.id.ibFastRewind);
        tvTime = findViewById(R.id.tvTime);
        mediaPlayer = MediaPlayer.create(this, R.raw.astronaut);
        skbMusicPlayer.setClickable(false);
        ibPlay = findViewById(R.id.ibPlay);
        ibPlay.setOnClickListener(v -> playMusic());
        ibPause = findViewById(R.id.ibPause);
        ibPause.setOnClickListener(v -> pauseMusic());
        ibFastForward.setOnClickListener(v -> fastForward());
        ibFastRewind.setOnClickListener(v -> fastRewind());
    }

    private void fastRewind() {
        int temp = (int) startTime;
        if ((temp - backwardTime) > 0) {
            startTime = temp - backwardTime;
            mediaPlayer.seekTo((int) startTime);
        } else {
            Toast.makeText(this, "Cant go back!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fastForward() {
        int temp = (int) startTime;
        if ((temp + forwardTime) <= finalTime) {
            startTime = temp + forwardTime;
            mediaPlayer.seekTo((int) startTime);
        } else {
            Toast.makeText(this, "Cant fast forward!", Toast.LENGTH_SHORT).show();
        }
    }


    private void pauseMusic() {
        mediaPlayer.pause();
    }

    private void playMusic() {
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        tvTime.setText(String.format(Locale.US, "%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) finalTime), TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

        tvCurrentTime.setText(String.format(Locale.US, "%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

        if (oneTime == 1) {
            skbMusicPlayer.setMax((int) finalTime);
            oneTime = 0;
        }
        skbMusicPlayer.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime, 100);
    }

    private final Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            tvCurrentTime.setText(String.format(Locale.US, "%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) startTime), TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            startTime = mediaPlayer.getCurrentPosition();
            skbMusicPlayer.setProgress((int) startTime);
            handler.postDelayed(this, 100);
        }
    };


}