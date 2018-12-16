package activities;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.murat.m_onboarding.R;

import utils.CameraPreview;
import utils.OCR;


public class SplashActivity extends AppCompatActivity {

    public static Camera mCamera;
    public static CameraPreview cameraView;
    private Boolean idOCR = true;
    private Boolean faceOCR =false;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mPreview = new CameraPreview(this,this, null);

        scheduleSplashScreen();

        OCR.id_n=null;


    }


    private void scheduleSplashScreen() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                startActivity(new Intent(SplashActivity.this,InstructionsActivity.class));
                finish();

            }
        }, 1500);


    }
}
