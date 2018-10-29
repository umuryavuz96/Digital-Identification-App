package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.murat.m_onboarding.R;

import utils.CameraPreview;

public class VoiceRecognitionActivity extends AppCompatActivity {
    private Button nextButton3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recognition);
        nextButton3=findViewById(R.id.nextButton3);

        nextButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToSign = new Intent(getBaseContext(), SignActivity.class);
                startActivity(goToSign);

            }
        });
    }
}
