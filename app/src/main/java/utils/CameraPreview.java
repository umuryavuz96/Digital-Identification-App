package utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

import activities.IDScanActivity;

import static android.content.ContentValues.TAG;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    public SurfaceHolder mHolder;
    private Context context;
    public CameraSource cameraSource;
    private Activity activity;
    final int RequestCameraPermissionID = 1001;


    public CameraPreview(final Context context, Camera camera, Activity activity) {
        super(context);
        mCamera = camera;
        this.context = context;
        this.activity = activity;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available");
        } else {

            cameraSource = new CameraSource.Builder(context, textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
        }

        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {

                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if(items.size() != 0)
                {
                  Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder stringBuilder = new StringBuilder();
                            for(int i =0;i<items.size();++i)
                            {
                                TextBlock item = items.valueAt(i);
                                stringBuilder.append(item.getValue());
                                stringBuilder.append("\n");
                            }
                            Toast.makeText(context,stringBuilder.toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                  t.start();
                }
            }
        });

    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
//        try {
//
//            mCamera.setPreviewDisplay(holder);
//            mCamera.startPreview();

        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        RequestCameraPermissionID);
                return;
            }
            cameraSource.start(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        } catch (IOException e) {
//            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
//        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
//          mCamera.stopPreview();
            cameraSource.stop();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {

//            mCamera.setPreviewDisplay(mHolder);
//            mCamera.startPreview();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        RequestCameraPermissionID);
                return;
            }
            cameraSource.start(mHolder);

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public static void closeCameraAndPreview(Camera mCamera,CameraPreview mPreview, FrameLayout preview) {
        if (mCamera != null) {
            mCamera .stopPreview();
            mCamera .setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mCamera .release();
            mCamera = null;
        }
        preview.removeView(mPreview);
    }

}