package com.berunda.audio.audioapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.BaseMediaSource;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class AudioPlayerService extends Service {

    public static SimpleExoPlayer player;
    ArrayList<Uri> songs = new ArrayList<Uri>();
    private PlayerNotificationManager PlayerNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        final Context context = this;

        songs.add(Uri.parse("http://209.133.216.3:7048/"));
        songs.add(Uri.parse("http://209.133.216.3:7073/;stream.mp3"));//Shree
        songs.add(Uri.parse("http://209.133.216.3:7018/stream")); //hiru
        songs.add(Uri.parse("http://69.46.24.226:7669/stream"));//Neth
        songs.add(Uri.parse("http://108.61.34.50:7130/;"));//Derana
        songs.add(Uri.parse("http://s3.voscast.com:8408"));//Siyatha
        songs.add(Uri.parse("http://220.247.227.20:8000/citystream"));//City


        //creates instance of player
        player= ExoPlayerFactory.newSimpleInstance(this,
                new DefaultTrackSelector());

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "AudioApp"));

        //create playlist by adding sources to ConcatenatingMediaSource
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        for (Uri temp : songs)
        {
            BaseMediaSource mediaSourse=
                    new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(temp);
            concatenatingMediaSource.addMediaSource(mediaSourse);
        }



        player.prepare(concatenatingMediaSource);
        player.setPlayWhenReady(true);

        //show player in notification bar
        PlayerNotificationManager =  PlayerNotificationManager.createWithNotificationChannel(
                context, "Chamara", R.string.exo_download_notification_channel_name, 2,
                new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        return "Amarabandu Ruupasinghe";
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        Intent intent = new Intent(context,MainActivity.class);
                        return PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
                        return "Hitan jokes Album";
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback callback) {
                        return null;
                    }
                }
        );

        PlayerNotificationManager.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {
                startForeground(notificationId, notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                stopSelf();
            }
        });


        PlayerNotificationManager.setPlayer(player);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        PlayerNotificationManager.setPlayer(null);
        player.release();
        player=null;

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

}

