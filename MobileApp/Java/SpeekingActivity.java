package com.example.speech_v10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SpeekingActivity extends AppCompatActivity {
    private ImageButton imageButton;
    private EditText editText;
    private boolean speakFlag = false;

    private SpeechRecognizer speechRecognizer;


    String clientId = MqttClient.generateClientId();
    MqttAndroidClient client =
            new MqttAndroidClient(SpeekingActivity.this, "tcp://" + QrscanActivity.ip_addr + ":1883",
                    clientId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speeking);

        imageButton = findViewById(R.id.SpeakButton);
        editText = findViewById(R.id.edittext);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WAKE_LOCK}, 1);
        }


        configureSpeechButton();
        configureSpeech();

        //MqTT connection


        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(SpeekingActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(SpeekingActivity.this, "Not connected", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


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

    public void configureSpeechButton() {
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click); // Sound from zapsplat -> ToChange

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(speakFlag==false)
                {
                    editText.setText("");
                    imageButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_24));
                    mediaPlayer.start();

                    //Start listening
                    speechRecognizer.startListening(speechRecognizerIntent);
                    speakFlag=true;
                }
                else
                {
                    imageButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_off_24));
                    mediaPlayer.start();

                    //Stop listening
                    speechRecognizer.stopListening();
                    speakFlag=false;



                }
            }
        });
    }

    public void configureSpeech() {
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                editText.setText(data.get(0));

                //MQTT_PUBLISH
                String topic = "test/status";
                String payload = editText.getText().toString();
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }
}