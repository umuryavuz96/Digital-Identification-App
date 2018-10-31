package activities;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.murat.m_onboarding.R;

import utils.CameraPreview;

import static utils.Camera.getCameraInstance;

public class FaceScanActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;

    private Button nextButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_scan);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera,this);
        mCamera.setDisplayOrientation(90);
        final FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);


        nextButton2 = findViewById(R.id.nextButton2);

        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPreview.closeCameraAndPreview(mCamera,mPreview, preview);
                Intent goToVoiceRecognition = new Intent(getBaseContext(), VoiceRecognitionActivity.class);
                startActivity(goToVoiceRecognition);

            }
        });


    }
}