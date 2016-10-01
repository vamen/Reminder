package com.vivekbalachandra.android.reminder;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.ibm.watson.developer_cloud.speech_to_text.v1.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class speechReciver extends AppCompatActivity   {
    class NetworkOperatioin extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            service = new SpeechToText();
            service.setUsernameAndPassword( ConstantData.speechToText_Usr_Watson , ConstantData.speechToText_pwd_Watson);

            options = new RecognizeOptions().contentType("audio/wav")
                    .continuous(true).interimResults(true).model("en-US_NarrowbandMode");
            delegate = new BaseRecognizeDelegate() {
                @Override
                public void onMessage(SpeechResults speechResults) {
                   // Log.d(TAG+"result :",speechResults.toString());
                    parseJson(speechResults.toString());
                }

                @Override
                public void onError(Exception e) {
                    //e.printStackTrace();
                    Log.d(TAG+"result","error :"+ e);
                }
            };
            InputStream ins = getResources().openRawResource(
                    getResources().getIdentifier("man1_nb",
                            "raw", getPackageName()));
            service.recognizeUsingWebSockets(ins,
                    options, delegate);
            return null;
        }
    }

    private void parseJson(String s) {
        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONArray res=jsonObject.getJSONArray("results");
            JSONObject text=res.getJSONObject(0);
            JSONArray resultArray=text.getJSONArray("alternatives");
            JSONObject finalTextObject=resultArray.getJSONObject(0);
            String finalText=finalTextObject.getString("transcript");
            //Log.d(TAG+"parsed",text.toString());
            Log.d(TAG + "parsed", finalText);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final String TAG=speechReciver.class.getSimpleName();
    Button listen;
    SpeechToText service;
    RecognizeOptions options;
    BaseRecognizeDelegate delegate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_speech_reciver);

        listen= (Button) findViewById(R.id.startRecognizing);


         listen.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getAudio();
                 new NetworkOperatioin().execute();
             }
         });



    }

    private void getAudio() {
        if(isExternalStorageWritable()) {
            File file = new File(ConstantData.filePath+"audio"+".3gp");

            MediaRecorder recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(file);
            recorder.prepare();
            recorder.start();
        }
        }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }




}
