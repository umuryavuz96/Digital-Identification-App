package activities;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.murat.m_onboarding.R;
import android.os.Handler;

import utils.CameraPreview;
import utils.OCR;

import static utils.Camera.getCameraInstance;


public class SplashActivity extends AppCompatActivity {

    public static Camera mCamera;
    public static CameraPreview cameraView;
    private Boolean idOCR = true;
    private Boolean faceOCR =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        scheduleSplashScreen();

        //reset fields for restart
        OCR.id_n=null;

//        mCamera = getCameraInstance();
//        cameraView  = new CameraPreview(this, mCamera,this,idOCR);

       /*
        Thread splash = new Thread() {
            @Override
            public void run() {
                try {
                    Intent goToInstructions = new Intent(SplashActivity.this,InstructionsActivity.class);
                    sleep(2500);
                    startActivity(goToInstructions);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        splash.start();

        */
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
