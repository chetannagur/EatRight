package com.example.eatright;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class single_scan extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA=1;
    private  ZXingScannerView ScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView =new ZXingScannerView(this);
        setContentView(ScannerView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);{
            if (checkPermission ())
            {
                Toast.makeText(single_scan.this, "Permission Is Granted", Toast.LENGTH_LONG).show();
            }
            else
            {

                requestPermission();
            }
        }
    }
    private  boolean  checkPermission()
    {
        boolean b = ContextCompat.checkSelfPermission(single_scan.this, CAMERA) == PackageManager.PERMISSION_GRANTED;
        return b;



    }
    private  void requestPermission()
    {


        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);


    }
    public void onRequestPermissionResult(int requestCode,String permission[],int grantResults[])
    {
        switch (requestCode)
        {
            case REQUEST_CAMERA:
                if (grantResults.length > 0)
            {
                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted)
                {
                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                }
           else
                {

                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (shouldShowRequestPermissionRationale(CAMERA)){
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener(){

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                                requestPermissions(new String[]{CAMERA},
                                                        REQUEST_CAMERA);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
            }
                break;
        }
    }
    public void onResume() {

        super.onResume();



        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {

            if (checkPermission()) {
                if(ScannerView == null) {
                    ScannerView = new ZXingScannerView(this);
                    setContentView(ScannerView);
                }
                ScannerView.setResultHandler(this);
                ScannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }
    public void onDestroy() {
        super.onDestroy();
        ScannerView.stopCamera();
    }

    private void showMessageOKCancel(String messege, DialogInterface.OnClickListener okListener) {

        new AlertDialog.Builder(single_scan.this)
                .setMessage(messege)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {

        final String myResult = result.getText();

        Log.d("QRCodeScanner", result.getText());

        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());



        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Scan Result");

        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                ScannerView.resumeCameraPreview(single_scan.this);

            }

        });

        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myResult));

                startActivity(browserIntent);

            }

        });

        builder.setMessage(result.getText());

        AlertDialog alert1 = builder.create();

        alert1.show();


    }
}
