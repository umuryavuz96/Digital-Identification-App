package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.FrameLayout;

import activities.FaceScanActivity;
import models.ID;
import utils.CameraPreview;

import com.example.murat.m_onboarding.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class OCR{

    public TextRecognizer textRecognizer;
    private Context context;
    private StringBuilder stringBuilder;
    private Activity activity;
    private ArrayList<String> item_list;


    static public Long id_n;
    private String date_of_birth;
    private String name;
    private String surname;

    public Resources resources;
    public Boolean id_detected = false;

    public int image = R.drawable.kimlik;

    static public ID id_instance;

    private CameraPreview cameraPreview;

    public OCR(Context context, Activity activity,CameraPreview cameraPreview){
        this.context = context;
        this.activity = activity;
        this.cameraPreview = cameraPreview;
        textRecognizer = new TextRecognizer.Builder(context).build();

        item_list = new ArrayList<>();

        id_instance = new ID();

        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available");
        } else {
            CameraPreview.setCameraSource(textRecognizer);
        }


    }

    public static boolean isNumeric(String str) {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
    public void setOCRprocessor_Image(final byte[] image_bitmap) {
        System.out.print("CAPTURE IMAGE OCR PROCESSİNG");

        resources = context.getResources();
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {

                //TODO : Rotate the taken photo for ocr process over landscaped photo
                 Bitmap bitmap = BitmapFactory.decodeByteArray(image_bitmap, 0,image_bitmap.length);

                Frame frame = new Frame.Builder().setBitmap(bitmap).build();

                final SparseArray<TextBlock> items = textRecognizer.detect(frame);

                if (items.size() != 0) {
                    stringBuilder = new StringBuilder();

                    for (int i = 0; i < items.size(); ++i) {
                        TextBlock item = items.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("\n");


                    }

                    parseID();
                    Log.w("OCR", stringBuilder.toString());
                    Intent goToFaceScan = new Intent(context, FaceScanActivity.class);
                    activity.startActivity(goToFaceScan);
                    activity.finish();
                }

            }
        });
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
                            stringBuilder = new StringBuilder();
                            for(int i =0;i<items.size();++i)
                            {
                                TextBlock item = items.valueAt(i);
                                stringBuilder.append(item.getValue());
                                if(isNumeric(item.getValue().toString())) {
                                    if (item.getValue().toString().length() == 11) {
                                        id_n = Long.parseLong(item.getValue());
                                          id_detected = true;
                                          cameraPreview.captureImage();
                                        Log.w("OCR","ID DETECTED");
                                    }
                                }
                                stringBuilder.append("\n");
                            }
                        }
                    });
                    t.start();

                    if (id_detected){
                        t.destroy();
                    }
                }
            }
        });



    }

    public void parseID(){

        StringTokenizer tokenizer = new StringTokenizer(stringBuilder.toString(),"\n");
        item_list = new ArrayList<>();
        while(tokenizer.hasMoreElements()){
            item_list.add(tokenizer.nextElement().toString());
        }

        for(int index = 0;index<item_list.size();index++){
            if(item_list.get(index).equals("TC. Kmtik No / TR Identity No")){
                id_instance.setID(Long.parseLong(item_list.get(index+1)));
            }else{
                id_instance.setID(id_n);
            }

            if(item_list.get(index).equals("Soyadi/")){
                id_instance.setSURNAME(item_list.get(index+1));
            }

            if(item_list.get(index).equals("Adi/ Given Name(s)")){
                id_instance.setNAME(item_list.get(index+1));
            }

            if(item_list.get(index).equals("Dogum Tarih / Date Of Birth Cnsiyet / Gender")){
                id_instance.setDATE_OF_BIRTH(item_list.get(index+1));
            }

            if(item_list.get(index).equals("Seri No/")){
                id_instance.setSERIAL_NO(item_list.get(index+1));
            }

            if(item_list.get(index).equals("Son Gecerlilik/ Valid Until")){
                id_instance.setVALID_UNTIL(item_list.get(index+1));
            }

            if(item_list.get(index).equals("Uyrugu/Nationality")){
                id_instance.setNATIONALITY(item_list.get(index+1));
            }

        }


    }
}
