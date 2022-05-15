package com.example.agenda;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import java.io.File;

public class MediaSessionService extends Service {
    public MediaPlayer mediaPlayer;
    public static final String TAG = "MediaSessionService";
    public static final int NOTIFICATION_ID = 888;
    private MediaNotificationManager mMediaNotificationManager;
    private MediaSessionCompat mediaSession;
    public static MediaSessionService servico;

    @Override
    public void onCreate() {
        super.onCreate();
        servico = this;
        mediaPlayer = new MediaPlayer();
        mMediaNotificationManager = new MediaNotificationManager(this);
        mediaSession = new MediaSessionCompat(this, "SOME_TAG");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                System.out.println("play");
               // mediaPlayer.start();
            }

            @Override
            public void onPause() {
                System.out.println("pouse");
               // mediaPlayer.pause();
            }
        });
        Notification notification =
                mMediaNotificationManager.getNotification(
                        getMetadata(), getState(), mediaSession.getSessionToken());

        startForeground(NOTIFICATION_ID, notification);
    }

    public MediaMetadataCompat getMetadata() {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();

        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "artist");
        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, MainActivity.main.javaScript.musica);
        builder.putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration()
        );
        return builder.build();
    }

    private PlaybackStateCompat getState() {
      //  long actions = mediaPlayer.isPlaying() ? PlaybackStateCompat.ACTION_PAUSE : PlaybackStateCompat.ACTION_PLAY;
     //   int state = mediaPlayer.isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
        long actions = !MainActivity.main.javaScript.tocando ? PlaybackStateCompat.ACTION_PAUSE : PlaybackStateCompat.ACTION_PLAY;
        int state = !MainActivity.main.javaScript.tocando ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
        final PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder();
        stateBuilder.setActions(actions);
        stateBuilder.setState(state,
                mediaPlayer.getCurrentPosition(),
                1.0f,
                SystemClock.elapsedRealtime());
        return stateBuilder.build();
    }

    public void refresh()
    {
        Notification notification =
                mMediaNotificationManager.getNotification(
                        getMetadata(), getState(), mediaSession.getSessionToken());

        startForeground(NOTIFICATION_ID, notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ("android.intent.action.MEDIA_BUTTON".equals(intent.getAction())) {
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get("android.intent.extra.KEY_EVENT");
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                MainActivity.main.view.loadUrl("javascript:VUE.forcePlay()");
                refresh();
              //  mediaPlayer.pause();
            } else {
               MainActivity.main.view.loadUrl("javascript:VUE.forcePlay()");
                refresh();
              //  mediaPlayer.start();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}