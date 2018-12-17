package utils;

import android.Manifest;
import android.annotation.SuppressLint;
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

    public SurfaceHolder mHolder;
    private static Context context;
    public static CameraSource cameraSource;
    private static Activity activity;
    final int RequestCameraPermissionID = 1001;
    public static OCR OCR;
    public static boolean safeToTakePicture = false;
    public FaceDetectAndCrop faceDetectAndCrop;
    public static CameraPreview instance;
    private FrameLayout layout;


    public CameraPreview(final Context context, final Activity activity,FrameLayout layout) {
        super(context);

        this.context = context;
        this.activity = activity;
        this.layout = layout;

        instance = this;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    public void setFaceDetectAndCrop(FaceDetectAndCrop faceDetectAndCrop) {
        this.faceDetectAndCrop = faceDetectAndCrop;
    }

    public void setLayout(FrameLayout layout) {
        this.layout = layout;
    }

    public FrameLayout getLayout() {
        return layout;
    }

    public void setContext(Context context) {
        CameraPreview.context = context;
    }

    public void setActivity(Activity activity) {
        CameraPreview.activity = activity;
    }

    public void setOCR(utils.OCR OCR) {
        CameraPreview.OCR = OCR;
        CameraPreview.OCR.setOCRprocessor();
    }

    public static CameraPreview get_instance(){
        return instance;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if(OCR != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA},
                            RequestCameraPermissionID);
                    return;
                }
            }
            cameraSource.start(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void releaseCameras() {
        cameraSource.stop();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    @SuppressLint("MissingPermission")
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            cameraSource.stop();
        } catch (Exception e) {

        }
        try {

            cameraSource.start(mHolder);
            safeToTakePicture = true;
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public static void setCameraSource(final TextRecognizer textRecognizer) {
        cameraSource = new CameraSource.Builder(context, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 720 )
                .setRequestedFps(60.0f)
                .setAutoFocusEnabled(true)
                .build();


    }

    public void setCameraSource(final FaceDetector faceDetector,Context c) {
        cameraSource = new CameraSource.Builder(c, faceDetector)
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

                    if (safeToTakePicture) {
                        float left = 0;
                        float top = 0;
                        float right = 0;
                        float bottom = 0;
                        Face face = items.valueAt(0);
                        left = (float) (face.getPosition().x);
                        top = (float) (face.getPosition().y);
                        right = (float) (face.getWidth());
                        bottom = (float) (face.getHeight());
                        captureImage(left, top, right, bottom);
                    }
                }
            }
        });
    }

    public void captureImage() {

        //OCR.stop_ocr();
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

    public void captureImage(final float left, final float top, final float right, final float bottom) {
        Log.w("CAPTURE", "CAPTURE IMAGE");
        cameraSource.takePicture(null, new CameraSource.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                Bitmap face = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                faceDetectAndCrop.setBitmap(face,left,top,right,bottom);
                safeToTakePicture = true;
            }
        });


    }

    public static void check_ID_validity(){
        Log.w("CAPTURE", "Checking ID validity");
        cameraSource.takePicture(null, new CameraSource.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                ID_Validity id_validity = new ID_Validity(context,activity);
                id_validity.execute(bytes);
            }
        });
    }

    public void startCameraSource(SurfaceHolder holder) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            cameraSource.start(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset_instances(){
        this.OCR = null;
        context = null;
        activity = null;
    }

    public void initOCR(){
        OCR.start_ocr();
    }
}
