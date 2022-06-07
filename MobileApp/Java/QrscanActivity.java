package com.example.speech_v10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;


public class QrscanActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private CodeScannerView scannerView;
    public static String ip_addr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        scannerView = findViewById(R.id.scanner_view);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        }

        configureScanner();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    public void configureScanner() {

        mCodeScanner = new CodeScanner(this,scannerView);
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeScanner.startPreview();
            }

        });

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: 06.04.2022 device connection
                        //Toast.makeText(QrscanActivity.this,result.getText(),Toast.LENGTH_LONG);
                        ip_addr = result.getText();
                        startActivity(new Intent(QrscanActivity.this, SpeekingActivity.class));
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT);
            }
        }
        else
        {
            Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT);
        }
    }

}