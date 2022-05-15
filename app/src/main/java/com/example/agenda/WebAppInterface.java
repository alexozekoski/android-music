package com.example.agenda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.RemoteViews;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.github.kiulian.downloader.OnYoutubeDownloadListener;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.model.VideoDetails;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioFormat;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.graphics.drawable.Icon.createWithResource;

public class WebAppInterface {
    Activity main;
    WebView view;
    /**
     * Instantiate the interface and set the context
     */
    File base;

    String id;
    NotificationManager notificationManager;
    Notification notification;

    boolean tocando = false;

    WebAppInterface(Activity main, WebView view, String id) {
        this(main, view);
        this.id = id;
    }

    @JavascriptInterface
    public void compartilharCom(String id)
    {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);

        sendIntent.putExtra(Intent.EXTRA_TITLE, "Musica");

        System.out.println(base.toURI());
        Uri uri = FileProvider.getUriForFile(
                main,
                "com.example.agenda.files",
                new File(base, id + ".mp3"));
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("audio/*");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Musica");
        sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Show the Sharesheet
        this.main.startActivity(Intent.createChooser(sendIntent, "Compartilhar musica"));
    }

    @JavascriptInterface
    public void tocando(boolean tocando)
    {
        this.tocando = tocando;
    }

//    @SuppressLint("WrongConstant")
//    public void addNotification()
//    {
//        createNotificationChannel();
//        Intent intent = new Intent("show");
//        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(main, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        RemoteViews notificationLayout = new RemoteViews(main.getPackageName(), R.layout.notification);
//        RemoteViews notificationLayoutExpanded = new RemoteViews(main.getPackageName(), R.layout.notification);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(main, "musica");
//        builder.setContentTitle("Musica");
//        builder.setContentText("Texto");
//        builder.setSmallIcon(R.drawable.logo);
//        builder.setPriority(NotificationCompat.PRIORITY_MAX);
//        builder.addAction(new NotificationCompat.Action(R.drawable.logo, "Play", pendingNotificationIntent));
//        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
//        builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(null));
//        builder.setCustomContentView(notificationLayout);
//        builder.setCustomBigContentView(notificationLayoutExpanded);
//        builder.setVisibility(Notification.VISIBILITY_PUBLIC);
//
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        MediaNotificationManager mMediaNotificationManager = new MediaNotificationManager(this);
//        MediaSessionCompat mediaSession = new MediaSessionCompat(main, "SOME_TAG");
//        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
//        mediaSession.setCallback(new MediaSessionCompat.Callback() {
//            @Override
//            public void onPlay() {
//            //    mediaPlayer.start();
//            }
//
//            @Override
//            public void onPause() {
//               // mediaPlayer.pause();
//            }
//        });
//        Notification notification =
//                mMediaNotificationManager.getNotification(
//                        getMetadata(), getState(), mediaSession.getSessionToken());
//
//        main.startForeground(NOTIFICATION_ID, notification);
//
////       Notification.Builder builder = new Notification.Builder(main)
////                .setVisibility(Notification.VISIBILITY_PUBLIC)
////               .setContentText("Teste")
////               .setContentTitle("Musica")
////                .setSmallIcon(R.drawable.logo);
//////                .addAction(R.drawable.logo, "button1",ButtonOneScreen)
//////                .addAction(R.drawable.logo, "button2", ButtonTwoScreen);
//////
//////        Notification.MediaStyle style = new Notification.MediaStyle();
//////        style.setShowActionsInCompactView(1, 1);
//////            .setMediaSession(mMediaSession.getSessionToken())
//////            .setContentTitle("your choice")
//////            .setContentText("Again your choice")
//////            .setLargeIcon(buttonIcon)
//////            .build();
//////        String ns = Context.NOTIFICATION_SERVICE;
//////        notificationManager = (NotificationManager) main.getSystemService(ns);
//////        notification = new Notification(R.mipmap.ic_launcher, null, System.currentTimeMillis());
//////        //the intent that is started when the notification is clicked (works)
//////        Intent intent = new Intent("show");
//////        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(main, 0,
//////                intent, 0);
////
//////
//////
//        Notification notification = builder.build();
//                notification.contentIntent = pendingNotificationIntent;
//       // notification.flags |= Notification.FLAG_NO_CLEAR;
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(main);
//        notificationManager.notify(1000, builder.build());
////        notificationManager.notify(1, notification);
//    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "musica";
            String description = "Músicas";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("musica", name, importance);
            channel.setDescription(description);
           // channel.setShowBadge(false);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = main.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
          //  notificationManager.createNotificationChannelGroup(new NotificationChannelGroup("grupo", "grupo"));

        }
    }

    WebAppInterface(Activity main, WebView view) {
        this.main = main;
        base = main.getFilesDir();
        this.view = view;

       // addNotification();
        //clear();
        // scan();
    }
    boolean servico = false;
    String musica = "";
    @JavascriptInterface
    public void colocarServico(String musica)
    {
        this.musica = musica;
        if(servico)
        {
            MediaSessionService.servico.refresh();
            return;
        }
        servico = true;
        ContextCompat.startForegroundService(
                main.getApplicationContext(),
                new Intent(main.getApplicationContext(), MediaSessionService.class));
    }
    @JavascriptInterface
    public String id() {
        return id;
    }

    @JavascriptInterface
    public boolean write(String dir, String json) {
        File file = new File(base, "/" + dir);
        try {
            String[] pastas = dir.split("/");
            dir = "";
            for (int i = 0; i < pastas.length - 1; i++) {
                dir += "/" + pastas[i];
                File pasta = new File(base, dir);
                if (!pasta.exists()) {
                    pasta.mkdir();
                }
            }
            FileOutputStream saida = new FileOutputStream(file);
            saida.write(json.getBytes());
            saida.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @JavascriptInterface
    public String read(String dir) {
        File file = new File(base, "/" + dir);
        if (file.exists()) {
            try {
                FileInputStream entrada = new FileInputStream(file);
                byte[] dados = new byte[(int) file.length()];
                entrada.read(dados);
                entrada.close();
                return new String(dados);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    @JavascriptInterface
    public void toast(final String toast) {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(main, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @JavascriptInterface
    public void sair() {
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                main.finish();
            }
        });
    }

    @JavascriptInterface
    public String find(String id)
    {
        return new File(base, id + ".mp3").toURI().toString();
    }


    @JavascriptInterface
    public void apagar(String id)
    {
         new File(base, id + ".mp3").delete();
    }

    @JavascriptInterface
    public String meta(final String id) {
        try {
            YoutubeDownloader downloader = new YoutubeDownloader();

            downloader.addCipherFunctionPattern(2, "\\b([a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)");
            downloader.setParserRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
            downloader.setParserRetryOnFailure(1);

            final YoutubeVideo video = downloader.getVideo(id);

            final List<AudioFormat> audioFormats = video.audioFormats();
            JsonObject json = new JsonObject();
            json.addProperty("nome", video.details().title());
            json.addProperty("tumb", video.details().thumbnails().get(video.details().thumbnails().size() - 1));
            json.addProperty("id", id);
            json.addProperty("duracao", audioFormats.get(audioFormats.size() - 1).duration());
            json.addProperty("url", audioFormats.get(audioFormats.size() - 1).url());
            return json.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @JavascriptInterface
    public void download(final String url) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream saida = new FileOutputStream(new File(base, id + ".mp3"));
                   saida.write(url(url, "GET", id));
                    saida.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    view.loadUrl("javascript:VUE.erro('" + id + "')");
                }
            }
        });
        t.start();

    }

    public byte[] url(String u, String metodo, final String id) throws MalformedURLException, IOException {

        URL url = new URL(u);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(metodo);
        connection.setConnectTimeout(10000);
        InputStream in = connection.getInputStream();
        int size = connection.getContentLength();
        byte[] buffer = new byte[size];

        int p = 0;
        int l;
        int bloco = (int) (size * 0.01);
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.loadUrl("javascript:VUE.progresso(0, '" + id + "')");
            }
        });
        double next = bloco;
        do {
            int t = bloco;
            if ((buffer.length - p) < bloco) {
                t = buffer.length - p;
            }
            l = in.read(buffer, p, t);
            if (l == -1) {
                break;
            } else {
                p += l;
                if (p > next) {
                    System.out.println(100 * next / size);
                    final String script = "javascript:VUE.progresso(" + (100 * p / size) + ", '" + id + "')";
                    main.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.loadUrl(script);
                        }
                    });
                    next += bloco;
                }
            }

        } while (p < buffer.length);
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.loadUrl("javascript:VUE.progresso(100, '" + id + "')");
                view.loadUrl("javascript:VUE.finalizado('" + id + "')");
            }
        });
        return buffer;
    }

}
