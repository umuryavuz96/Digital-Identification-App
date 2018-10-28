package activities;

import android.content.Intent;
import android.hardware.Camera;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.murat.m_onboarding.R;

import org.w3c.dom.Text;

import utils.CameraPreview;

import static utils.Camera.getCameraInstance;

public class IDScanActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;

    private ImageView id_template;
    private TextView instruction_1;
    private TextView instruction_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idscan);



        // Create an instance of Camera
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        mCamera.setDisplayOrientation(90);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);


        id_template = (ImageView) findViewById(R.id.id_template);
        instruction_1 = (TextView) findViewById(R.id.id_template_ins_1);
        instruction_2 = (TextView) findViewById(R.id.id_template_ins_2);






    }

    @Override
    protected void onStart() {
        super.onStart();
        Thread splash = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        instruction_2.setVisibility(View.INVISIBLE);
                        wait(10000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                id_template.setVisibility(View.INVISIBLE);
                                instruction_1.setVisibility(View.INVISIBLE);
                                instruction_2.setVisibility(View.VISIBLE);
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
