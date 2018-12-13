package com.berunda.audio.audioapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {

    //private PlayerView playerview;
    private PlayerControlView playerControlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AudioPlayerService as = new AudioPlayerService();

        Intent intent = new Intent(this,AudioPlayerService.class);
        Util.startForegroundService(this,intent);

        playerControlView = findViewById(R.id.player_Control_View);

        playerControlView.setPlayer(as.player);


    }
}
