package utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

import activities.FaceScanActivity;
import activities.VoiceRecognitionActivity;

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
    public FaceDetectAndCrop faceDetectAndCrop;
    public static boolean safeToTakePicture = false;
    public boolean FaceScan = false;


    public CameraPreview(final Context context, final Activity activity, boolean ocr) {
        super(context);
        //mCamera = camera;
        this.context = context;
        this.activity = activity;
        this.ocr = ocr;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        if (ocr) {
            OCR = new OCR(context, activity, this);
            OCR.setOCRprocessor();
            faceDetectAndCrop = new FaceDetectAndCrop(context, activity, this, FaceScanActivity.class,true);
        } else {
            faceDetectAndCrop = new FaceDetectAndCrop(context, activity, this, VoiceRecognitionActivity.class,false);
        }


    }

    public void surfaceCreated(SurfaceHolder holder) {
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

    public void releaseCameras() {
        cameraSource.stop();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        try {
            cameraSource.stop();
        } catch (Exception e) {

        }
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        RequestCameraPermissionID);
                return;
            }
            cameraSource.start(mHolder);
            safeToTakePicture = true;
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }


    public static void setCameraSource(TextRecognizer textRecognizer) {
        cameraSource = new CameraSource.Builder(context, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1920, 1080)
                .setRequestedFps(60.0f)
                .setAutoFocusEnabled(true)
                .build();

    }

    public void setCameraSource(final FaceDetector faceDetector) {


        cameraSource = new CameraSource.Builder(context, faceDetector)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedPreviewSize(1920, 1080)
                .setRequestedFps(60.0f)
                .setAutoFocusEnabled(true)
                .build();

        faceDetector.setProcessor(new Detector.Processor<Face>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Face> detections) {
                final SparseArray<Face> items = detections.getDetectedItems();
                if (items.size() != 0) {
                    Log.w("FACE", "FACE DETECTED!!");
                    Bitmap face = null;
                    if (safeToTakePicture) {
                        captureImage();
                        safeToTakePicture = false;
                        faceDetector.release();

                    }


                }
            }
        });

    }

    public void captureImage() {
        Log.w("CAPTURE", "CAPTURE IMAGE");
        cameraSource.takePicture(null, new CameraSource.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                Bitmap face = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                faceDetectAndCrop.setBitmap(face);
                safeToTakePicture = true;

            }
        });


    }

    public void setFaceDetectAndCrop(FaceDetectAndCrop faceDetectAndCrop) {
        this.faceDetectAndCrop = faceDetectAndCrop;
    }

    public void startCameraSource(SurfaceHolder holder) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            cameraSource.start(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
