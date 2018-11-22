package utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.PixelCopy;
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

import activities.FaceScanActivity;
import activities.IDScanActivity;
import activities.TempImageClass;

import static android.content.ContentValues.TAG;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static Camera mCamera;
    public SurfaceHolder mHolder;
    private static Context context;
    public static CameraSource cameraSource;
    private Activity activity;
    final int RequestCameraPermissionID = 1001;
    private Boolean ocr = false;
    public static OCR OCR;
    public static boolean safeToTakePicture = false;



    public CameraPreview(final Context context, Camera camera, final Activity activity, Boolean ocr) {
        super(context);
        mCamera = camera;
        this.context = context;
        this.activity = activity;
        this.ocr = ocr;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        if (ocr) {
            OCR = new OCR(context, activity, this);
            OCR.setOCRprocessor();
        }




    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        if (!ocr) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
                safeToTakePicture = true;
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        } else {
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
        }


    }

    public static void releaseCameras(){
        cameraSource.stop();
        if(mCamera != null){
            mCamera.release();
        }
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
            if (ocr) {
                cameraSource.stop();
            } else {
                mCamera.stopPreview();
            }
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {

            if (ocr) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA},
                            RequestCameraPermissionID);
                    return;
                }
                cameraSource.start(mHolder);

            } else {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public static void closeCameraAndPreview(Camera mCamera, CameraPreview mPreview, FrameLayout preview) {

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
            mCamera = null;
        }
        preview.removeView(mPreview);
    }

    public static void setCameraSource(TextRecognizer textRecognizer) {
        cameraSource = new CameraSource.Builder(context, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(4.0f)
                .setAutoFocusEnabled(true)
                .build();

    }

    public void captureImage(){
            System.out.print("CAPTURE IMAGE");
        cameraSource.takePicture(null,IDScanActivity.mPictureCallBack);
    }

}
