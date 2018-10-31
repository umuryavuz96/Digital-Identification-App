package utils;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import utils.CameraPreview;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class OCR{

    public TextRecognizer textRecognizer;
    private Context context;

    public OCR(Context context){
        this.context = context;
        textRecognizer = new TextRecognizer.Builder(context).build();

        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available");
        } else {
            CameraPreview.setCameraSource(textRecognizer);
        }


    }


    public void setOCRprocessor(){
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
                            Log.w("OCR",stringBuilder.toString());
                        }
                    });
                    t.start();
                }
            }
        });
    }
}
