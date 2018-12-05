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
import utils.FaceDetectAndCrop;

import static utils.CameraKYC.getCameraInstance;

public class FaceScanActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Button nextButton2;
    private Boolean OCR = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_scan);

        // Create an instance of Camera

        //mCamera = getCameraInstance(true);
        //mCamera.setDisplayOrientation(90);
        mPreview = new CameraPreview(this,this,OCR);

        final FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);


        nextButton2 = findViewById(R.id.nextButton2);

        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CameraPreview.closeCameraAndPreview(mCamera,mPreview, preview);
                mPreview.releaseCameras();
                Intent goToVoiceRecognition = new Intent(getBaseContext(), VoiceRecognitionActivity.class);
                startActivity(goToVoiceRecognition);
                finish();


            }
        });



    }

   /* private Camera openFrontFacingCamera() {

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        Log.e("frontcam", "Camera count: " + cameraCount);
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.e("frontcam", "Camera info: " + cameraInfo.facing);
                Log.e("frontcam", "camIdx: " + camIdx);
                try {
                    cam = Camera.open(2);
                } catch (RuntimeException e) {
                    Log.e("frontcam", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }*/


}