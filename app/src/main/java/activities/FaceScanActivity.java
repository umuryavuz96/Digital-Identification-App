package activities;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.murat.m_onboarding.R;

import utils.CameraPreview;

import static utils.Camera.getCameraInstance;

public class FaceScanActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_scan);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        mCamera.setDisplayOrientation(90);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);




    }
}