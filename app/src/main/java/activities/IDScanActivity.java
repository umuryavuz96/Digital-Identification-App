package activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.murat.m_onboarding.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;

import org.w3c.dom.Text;

import java.io.IOException;

import utils.CameraPreview;

import static utils.Camera.getCameraInstance;

public class IDScanActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;

    private ImageView id_template;
    private TextView instruction_1;


    private Button nextButton;

    private Boolean OCR = true;

    public static byte[] b;
    public static CameraSource.PictureCallback mPictureCallBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idscan);



        // Create an instance of Camera
//        mCamera = SplashActivity.mCamera;
//        mPreview = SplashActivity.cameraView;

        mCamera = getCameraInstance();
        Camera.Parameters params = mCamera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        params.setAutoExposureLock(true);
        params.setAutoWhiteBalanceLock(true);
        params.setSceneMode(Camera.Parameters.SCENE_MODE_LANDSCAPE);
        mCamera.setParameters(params);
        mPreview  = new CameraPreview(this, mCamera,this,OCR);




        final FrameLayout preview =  findViewById(R.id.camera_preview);
        preview.addView(mPreview);


        id_template =  findViewById(R.id.id_template);
        instruction_1 = findViewById(R.id.id_template_ins_1);


        nextButton=findViewById(R.id.nextButton);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CameraPreview.closeCameraAndPreview(mCamera,mPreview, preview);
                Intent goToFaceScan = new Intent(getBaseContext(), FaceScanActivity.class);
                startActivity(goToFaceScan);
                finish();

            }
        });

        mPictureCallBack = new CameraSource.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                System.out.print("CAPTURE CALLBACK");
                Intent goImg = new Intent(IDScanActivity.this, TempImageClass.class);
                b=bytes;
                startActivity(goImg);
                mPreview.OCR.setOCRprocessor_Image(bytes);
                finish();
            }
        };





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        mPreview.cameraSource.start(mPreview.mHolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Thread splash = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {

                        wait(10000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                id_template.setVisibility(View.INVISIBLE);
                                instruction_1.setVisibility(View.INVISIBLE);

                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        splash.start();
    }

}
