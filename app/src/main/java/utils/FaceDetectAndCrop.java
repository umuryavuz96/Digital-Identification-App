package utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

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
    private ProgressDialog progressDoalog;

    public FaceDetectAndCrop(final Context context, final Activity activity, final CameraPreview mPreview, final Class targetcontext, final boolean ocr) {
        this.context = context;
        this.mPreview = mPreview;
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

        if (!ocr){
            if(fd.isOperational()){
                mPreview.setCameraSource(fd);
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
                        face_img = face;

                        if(ocr){
                            OCR.face = face;
                        }
                        DETECT_DONE = true;
                        detector.release();
                        Intent goToVoiceRecognition = new Intent(context, targetcontext);
                        activity.startActivity(goToVoiceRecognition);
                        activity.finish();
                        mPreview.releaseCameras();
                        progressDoalog.dismiss();
                        return;

                    }

                }
            }
        });

    }


    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        crop_face.start();
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Please wait ...");
        progressDoalog.setTitle("Image processing");
        progressDoalog.show();


    }
}
