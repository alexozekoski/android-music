package com.example.agenda;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MainActivity extends Activity implements View.OnClickListener {

    public WebView view;
    public WebAppInterface javaScript;
    private MainAcao acao;

    public static MainActivity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        main = this;
        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE
                }, 1);


        view = (WebView) findViewById(R.id.web);
        WebSettings webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setNeedInitialFocus(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);
        }
        view.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
            }
        });
        javaScript = new WebAppInterface(this, view);
        view.addJavascriptInterface(javaScript,"Device");
        view.loadUrl("file:///android_asset/index2.html");

    }

    public void clear(String file) {
        File dir = new File(getFilesDir(), file);
        apagar(dir);
    }

    @Override
    protected void onDestroy() {
        Intent stopIntent = new Intent(this, MediaSessionService.class);
        stopService(stopIntent);
        super.onDestroy();
    }

    public void apagar(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    apagar(file);
                }
                try {
                    System.out.println("Delete -->" + file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                file.delete();
            }
        }
    }

    @Override
    public void onClick(View view) {


    }

    @Override
    protected void onResume() {
        super.onResume();
        view.loadUrl("javascript:VUE.atualizar()");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(acao != null)
        {
            acao.restorno(requestCode,resultCode,data);
        }else
        {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    view.loadUrl("javascript:window.dispatchEvent(new Event(\"keyBack\"));");
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

   
}