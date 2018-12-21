package activities;

import android.content.Intent;
import android.hardware.Camera;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.murat.m_onboarding.R;

import utils.CameraPreview;
import utils.FaceDetectAndCrop;

public class FaceScanActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Button nextButton2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_scan);


        mPreview = CameraPreview.get_instance();
        mPreview.setActivity(this);
        mPreview.setContext(this);


        mPreview.setFaceDetectAndCrop(new FaceDetectAndCrop(this,this));
        final FrameLayout preview = findViewById(R.id.camera_preview);

        if(mPreview.getLayout() != null){
            mPreview.getLayout().removeView(mPreview);
        }

        mPreview.setLayout(preview);
        preview.addView(mPreview);


        nextButton2 = findViewById(R.id.nextButton2);

        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPreview.releaseCameras();
                Intent goToVoiceRecognition = new Intent(getBaseContext(), VoiceRecognitionActivity.class);
                startActivity(goToVoiceRecognition);
                finish();


            }
        });



    }
}