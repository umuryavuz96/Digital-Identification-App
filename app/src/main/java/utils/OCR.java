package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import activities.FaceScanActivity;
import utils.CameraPreview;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;

public class OCR{

    public TextRecognizer textRecognizer;
    private Context context;
    private StringBuilder stringBuilder;
    private Activity activity;
    private ArrayList<String> string_list;


    static public Long id_n;
    private String date_of_birth;
    private String name;
    private String surname;

    public OCR(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        textRecognizer = new TextRecognizer.Builder(context).build();

        string_list = new ArrayList<>();

        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available");
        } else {
            CameraPreview.setCameraSource(textRecognizer);
        }


    }

    public static boolean isNumeric(String str)
    {
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
                            string_list = new ArrayList<>();
                            for(int i =0;i<items.size();++i)
                            {
                                TextBlock item = items.valueAt(i);
                                stringBuilder.append(item.getValue());
                                string_list.add(item.getValue());
                                if(isNumeric(item.getValue().toString())) {
                                    if (item.getValue().toString().length() == 11) {
                                        id_n = Long.parseLong(item.getValue());
                                        Intent goToFaceScan = new Intent(context, FaceScanActivity.class);
                                        activity.startActivity(goToFaceScan);
                                        activity.finish();
                                    }
                                }


                                stringBuilder.append("\n");
                            }


                            //TODO : below code will be removed or updated
                            for(int i=0;i<string_list.size();i++){
                                if(string_list.get(i).contains("Date") || string_list.get(i).contains("Birth")){
                                    if(i+1<string_list.size()) {
                                        date_of_birth = string_list.get(i + 1);
                                    }
                                }
                                if(string_list.get(i).contains("Surname")){
                                    if(i+1<string_list.size()) {
                                        surname = string_list.get(i + 1);
                                    }
                                }
                                if(string_list.get(i).contains("Given") || string_list.get(i).contains("Name")){
                                    if(i+1<string_list.size()) {
                                        name = string_list.get(i + 1);
                                    }
                                }
                            }
                            Log.w("OCR",stringBuilder.toString());
                            Log.w("OCR", "ID = "+id_n);
                            Log.w("OCR","Name = "+name);
                            Log.w("OCR","Surname = " + surname);
                            Log.w("OCR","Date of Birth = "+ date_of_birth);
                            Log.w("OCR","read: "+string_list);


                        }
                    });
                    t.start();


                }
            }
        });



    }
}
