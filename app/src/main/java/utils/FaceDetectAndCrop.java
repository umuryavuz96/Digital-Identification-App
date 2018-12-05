package utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.text.TextBlock;

import activities.FaceScanActivity;

public class FaceDetectAndCrop{

    private Bitmap mBitmap;
    private SparseArray<Face> mFaces;

    private Bitmap face_img;

    public static boolean DETECT_DONE = false;

    private Context context;
    private ImageView img;

    private FaceDetector detector;
    private StringBuilder stringBuilder;

    public FaceDetectAndCrop(Context context, ImageView targetView) {
        this.context = context;
        this.img = targetView;

        this.detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(true)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();

    }
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;



        if (detector.isOperational()){
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
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
                img.setImageBitmap(face_img);
                DETECT_DONE = true;
            }
            detector.release();
        }
    }


    public void setFaceDetector(){
        detector.setProcessor(new Detector.Processor<Face>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Face> detections) {
                final SparseArray<Face> items = detections.getDetectedItems();
                if(items.size() != 0)
                {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            for(int i =0;i<items.size();++i)
                            {
                                Log.w("FACE",items.toString());
                            }

                        }
                    });
                    t.start();


                }
            }
        });
    }
}
