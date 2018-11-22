package utils;

import android.hardware.Camera;

public class CameraKYC {

    public static Camera getCameraInstance(Boolean front){
        Camera c = null;
        try {
            if(front){
                c = Camera.open(1);
            }else {
                c = Camera.open(); // attempt to get a Camera instance
            }
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


}
