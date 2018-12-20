package utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.util.Log;
import android.util.SparseArray;

import com.example.murat.m_onboarding.R;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import activities.FaceScanActivity;
import activities.IDScanActivity;
import activities.SignActivity;
import activities.VoiceRecognitionActivity;

public class FaceDetectAndCrop{

    private Bitmap mBitmap;
    private SparseArray<Face> mFaces;

    public static Bitmap face_img;


    public static boolean DETECT_DONE = false;
    private Activity activity;
    private Context context;

    private FaceDetector detector;
    private CameraPreview mPreview;
    private FaceDetector fd;

    private Thread crop_face;
    private Thread crop_face2;
    private ProgressDialog progressDoalog;

    float left = 0;
    float top = 0;
    float right = 0;
    float bottom = 0;

    private boolean ocr = false;

    private Class target_context;

    public FaceDetectAndCrop(final Context context, final Activity activity) {
        this.context = context;
        this.mPreview = CameraPreview.get_instance();
        this.activity = activity;

        detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(true)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();

        fd = new FaceDetector.Builder(context)
                .setTrackingEnabled(true)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();

        set_ocr();
        setTargetContext();

        if (!ocr){
            if(fd.isOperational()){
                mPreview.setCameraSource(fd,context);
            }
        }


        crop_face = new Thread(new Runnable() {
            @Override
            public void run() {
                if (detector.isOperational()){
                    Frame frame = new Frame.Builder().setBitmap(mBitmap).build();
                    mFaces = detector.detect(frame);
                    if(mFaces.size()>0){
                        Log.w("FACE","DETECTED FACES :"+mFaces.size());

                        float left = 0;
                        float top = 0;
                        float right = 0;
                        float bottom = 0;

                        for (int i = 0; i < mFaces.size(); i++) {

                            Face face = mFaces.valueAt(i);
                            left = (float) (face.getPosition().x);
                            top = (float) (face.getPosition().y);
                            right = (float) (face.getWidth());
                            bottom = (float) (face.getHeight());
                        }

                        Bitmap face = Bitmap.createBitmap(mBitmap,(int) left, (int) top, (int) right, (int) bottom);


                        if(ocr){
                            OCR.face = face;
                        }else{
                            face_img = face;
                        }

                        DETECT_DONE = true;
                        detector.release();
                        Intent goToVoiceRecognition = new Intent(context, target_context);
                        activity.startActivity(goToVoiceRecognition);
                        activity.finish();
                        mPreview.releaseCameras();
                        mPreview.reset_instances();
                        progressDoalog.dismiss();
                        return;

                    }

                }
            }
        });

        crop_face2 = new Thread(new Runnable() {
            @Override
            public void run() {
                if (detector.isOperational()){
                        Bitmap face = Bitmap.createBitmap(mBitmap,(int) left, (int) top, (int) right, (int) bottom);

                        if(ocr){
                            OCR.face = face;
                        }else{
                            face_img = face;
                        }
                        DETECT_DONE = true;
                        detector.release();
                        Intent goToVoiceRecognition = new Intent(context, target_context);
                        activity.startActivity(goToVoiceRecognition);
                        activity.finish();
                        mPreview.releaseCameras();
                        mPreview.reset_instances();
                        progressDoalog.dismiss();

                        return;

                    }

                }
            });

    }


    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;

        crop_face.start();
        setProgressBar();
    }

    public void setBitmap(Bitmap bitmap,float left,float top,float right,float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;

        mBitmap = bitmap;
        crop_face2.start();
        setProgressBar();
    }

    public void setProgressBar(){
        IDScanActivity.icon_id_back.setImageResource(R.drawable.icon_id_back_fin);
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Please wait ...");
        progressDoalog.setTitle("Image processing");
        progressDoalog.show();
        progressDoalog.setCancelable(false);
    }

    public void set_ocr(){
        if(CameraPreview.OCR !=null){
            this.ocr = true;
        }
    }

    public void setTargetContext(){
        if(CameraPreview.OCR != null){
            this.target_context = FaceScanActivity.class;
        }else{
            this.target_context = VoiceRecognitionActivity.class;
        }
    }
}
