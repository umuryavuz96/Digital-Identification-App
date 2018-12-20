package activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.murat.m_onboarding.R;
import com.google.android.gms.vision.CameraSource;

import java.io.IOException;

import utils.CameraPreview;
import utils.FaceDetectAndCrop;
import utils.OCR;

import static utils.CameraKYC.getCameraInstance;

public class IDScanActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private ImageView id_template;
    private TextView instruction_1;
    private Button nextButton;
    private Boolean OCR = true;
    public static ImageView icon_id_front;
    public static ImageView icon_id_back;

    public static byte[] b;
    public static CameraSource.PictureCallback mPictureCallBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idscan);



        mPreview = CameraPreview.get_instance();
        mPreview.setActivity(this);
        mPreview.setContext(this);
        mPreview.setOCR(new OCR(this,this));
        mPreview.setFaceDetectAndCrop(new FaceDetectAndCrop(this,this));
        final FrameLayout preview =  findViewById(R.id.camera_preview);

        if(mPreview.getLayout() != null){
            mPreview.getLayout().removeView(mPreview);
        }

        mPreview.setLayout(preview);
        preview.addView(mPreview);

        id_template =  findViewById(R.id.id_template);
        instruction_1 = findViewById(R.id.id_template_ins_1);
        nextButton=findViewById(R.id.nextButton);
        icon_id_front=findViewById(R.id.icon_id_front);
        icon_id_back=findViewById(R.id.icon_id_back);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CameraPreview.closeCameraAndPreview(mCamera,mPreview, preview);
                mPreview.releaseCameras();
                Intent goToFaceScan = new Intent(getBaseContext(), FaceScanActivity.class);
                startActivity(goToFaceScan);
                finish();

            }
        });







    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mPreview.startCameraSource(mPreview.mHolder);

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

                        wait(2000);

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
