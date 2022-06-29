package com.example.eatright;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.io.IOException;

public class compare2 extends AppCompatActivity {
    private Button button2;

    SurfaceView surfaceView;
    CameraSource cameraSource;
    EditText textView;
    TextView t1;
    BarcodeDetector barcodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare2);
        button2 = (Button) findViewById(R.id.btn2);
        surfaceView=(SurfaceView)findViewById(R.id.camerapreview);
        textView=(EditText) findViewById(R.id.textView);
        t1=(TextView) findViewById(R.id.trans);
        t1.setText(getIntent().getStringExtra("data"));


        barcodeDetector=new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource=new CameraSource.Builder(this,barcodeDetector).setRequestedPreviewSize(640,480).build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                {

                }
                try {
                    cameraSource.start(holder);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode=detections.getDetectedItems();
                if(qrCode.size()!=0)
                {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator=(Vibrator)getApplicationContext().getSystemService(VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            textView.setText(qrCode.valueAt(0).displayValue);
                            surfaceView.setVisibility(View.INVISIBLE);
                            if(surfaceView.getVisibility()==View.INVISIBLE)
                            {
                                textView.setGravity(Gravity.CENTER);
                            }
                        }
                    });
                }
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();

            }
        });

    }
    public void openActivity3()
    {
        String data1=textView.getText().toString();
        String data2=t1.getText().toString();
        Intent intent = new Intent(this , Result.class);
        intent.putExtra("data2",data1);
        intent.putExtra("data1",data2);
        startActivity(intent);

    }

}
