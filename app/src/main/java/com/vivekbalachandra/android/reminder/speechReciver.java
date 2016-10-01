package com.vivekbalachandra.android.reminder;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

public class speechReciver extends AppCompatActivity implements {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speech_reciver);
        SpeechToText service = new SpeechToText();
        service.setUsernameAndPassword("{"+ ConstantData.speechToTextWatson +"}", "{"+ConstantData.speechTotEXTpwdWatson+"}");

        MediaRecorder mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat();

        SpeechResults speechResults=service.recognize();
    }
}
