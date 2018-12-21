package activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.Voice;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import com.example.murat.m_onboarding.R;

import utils.CameraPreview;

public class VoiceRecognitionActivity extends AppCompatActivity {
    final String[] testSentences = {
            "Mert eve git",
            "Mert bana bak",
            "Mert okula git",
            "Mert ata bak",
            "Mert eve gel",
            "Mert film izledi",
            "Mert kalemi tut",
            "Mert derse git",
            "Mert topa bak",
            "Mert proje yap",
            "Zeynep eve git",
            "Zeynep bana bak",
            "Zeynep okula git",
            "Zeynep ata bak",
            "Zeynep eve gel",
            "Zeynep film izledi",
            "Zeynep kalemi tut",
            "Zeynep derse git",
            "Zeynep topa bak",
            "Zeynep proje yap",
            "Emre kitap oku",
            "Emre ödev yap",
            "Emre okulu bitirdi",
            "Emre mezun oldu",
            "Emre askere gitti",
            "Emre askerden geldi",
            "Emre okuldan geldi",
            "Emre matematik biliyor",
            "Emre telefonla oynama",
            "Emre haber oku",
            "Beril kitap oku",
            "Beril ödev yap",
            "Beril okulu bitirdi",
            "Beril mezun oldu",
            "Beril askere gitti",
            "Beril askerden geldi",
            "Beril okuldan geldi",
            "Beril matematik biliyor",
            "Beril telefonla oynama",
            "Beril haber oku",
    };

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    private Button gotosign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recognition);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
       // checkPermission();

        gotosign = (Button) findViewById(R.id.gotosign);

        gotosign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotosing = new Intent(VoiceRecognitionActivity.this,SignActivity.class);
                startActivity(gotosing);
                finish();
            }
        });

        final EditText editText = findViewById(R.id.editText);
        final TextView displayOutput = findViewById(R.id.displayOutput);
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                "tr-TR");

        int randomText = new Random().nextInt(testSentences.length-1);
        displayOutput.setText(testSentences[randomText]);

        //displayOutput.setText("Mert eve git");
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


                String bestFit = matches.get(0);


                //displaying the first match
                if (matches != null)
                    editText.setText(matches.get(0));


                    if(displayOutput.getText().toString().equalsIgnoreCase(editText.getText().toString())){
                        Toast.makeText(VoiceRecognitionActivity.this,"Speech matched!",Toast.LENGTH_LONG).show();
                        Intent goToSign = new Intent(VoiceRecognitionActivity.this, SignActivity.class);
                        startActivity(goToSign);
                        finish();
                    }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        findViewById(R.id.button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editText.setHint("You will see input here");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        editText.setText("");
                        editText.setHint("Listening...");
                        break;
                }
                return false;
            }
        });

    }


    /*private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }
}
