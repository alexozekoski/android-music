package com.example.agenda;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.widget.VideoView;

import com.journeyapps.barcodescanner.CaptureActivity;

public class ScanActivity extends CaptureActivity {

    private VideoView videoView = null;
    private SurfaceHolder holder = null;
    private android.hardware.Camera camera = null;
    private static final String TAG = "Video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
       super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan);
//        videoView = (VideoView) findViewById(R.id.video);
//
//        try {
//            camera = android.hardware.Camera.open();
//            camera.lock();
//            camera.getParameters().setRotation(90);
//        } catch(RuntimeException re) {
//            re.printStackTrace();
//        }
//
//        holder = videoView.getHolder();
//
//        holder.addCallback(this);
    }



}