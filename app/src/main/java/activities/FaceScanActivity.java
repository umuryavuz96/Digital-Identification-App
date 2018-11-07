package activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.hardware.Camera.CameraInfo;

import com.example.murat.m_onboarding.R;
import com.google.android.gms.vision.CameraSource;

import java.io.IOException;

import utils.CameraPreview;

import static utils.Camera.getCameraInstance;

public class FaceScanActivity extends AppCompatActivity {

    static Camera mCamera = null;
    private CameraPreview mPreview;
    private Button nextButton2;
    private Boolean OCR = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_scan);

        // Create an instance of Camera

         mCamera = getCameraInstance();
        //mCamera=openFrontFacingCamera();
        mPreview = new CameraPreview(this, mCamera,this,OCR);
        mCamera.setDisplayOrientation(90);
        final FrameLayout preview = findViewById(R.id.camera_preview);

        preview.addView(mPreview);




        nextButton2 = findViewById(R.id.nextButton2);

        nextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CameraPreview.closeCameraAndPreview(mCamera,mPreview, preview);
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