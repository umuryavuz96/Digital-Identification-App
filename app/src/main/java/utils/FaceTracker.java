package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;

import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

class FaceTracker extends Tracker<Face> {
    // other stuff

    public static boolean FACE_DETECTED = false;
    @Override
    public void onNewItem(int faceId, Face face) {
        FACE_DETECTED = true;

    }

    @Override
    public void onUpdate(FaceDetector.Detections<Face> detectionResults,
                         Face face) {

    }

    @Override
    public void onMissing(FaceDetector.Detections<Face> detectionResults) {

    }

    @Override
    public void onDone() {

    }
}