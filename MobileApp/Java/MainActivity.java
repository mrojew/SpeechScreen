package com.example.speech_v10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button ScanButton, TestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScanButton=findViewById(R.id.ScanButton);
        TestButton=findViewById(R.id.TestButton);


        configureScanButton();
        configureTestButton();
    }

    public void configureScanButton() {

        ScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QrscanActivity.class));
            }
        });
    }

    public void configureTestButton() {
        TestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SpeekingActivity.class));
            }
        });
    }
}