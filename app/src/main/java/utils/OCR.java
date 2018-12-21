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
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import activities.FaceScanActivity;
import activities.IDScanActivity;
import activities.VoiceRecognitionActivity;
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
    private int validity_count = 0;


    public static Bitmap face;
    public static Bitmap face_byte;
    public static Thread ocr_process;
    private boolean checkID = true;



    public OCR(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        this.cameraPreview = CameraPreview.get_instance();
        textRecognizer = new TextRecognizer.Builder(context).build();

        item_list = new ArrayList<>();

        id_instance = new ID();


        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available");
        } else {
            textRecognizer.setFocus(1);
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
                    ocr_process = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            stringBuilder = new StringBuilder();
                            for(int i =0;i<items.size();++i)
                            {
                                TextBlock item = items.valueAt(i);
                                stringBuilder.append(item.getValue());
                                Log.w("OTEXT",validity_count+" items selected");
                                if(isNumeric(item.getValue().toString())) {
                                    if (item.getValue().toString().length() == 11) {
                                          id_n = Long.parseLong(item.getValue());
                                          String idstr= id_n.toString();
                                          IDChecksum checker= new IDChecksum();

                                          if(/*checker.validify(idstr)*/true){
                                              if(checkID) {
                                                  cameraPreview.check_ID_validity();
                                                  checkID = false;
                                              }

                                              if(ID_Validity.valid) {
                                                  id_detected = true;
                                                  Log.w("OCR", "ID DETECTED");
                                              }else{
                                                  return;
                                              }
                                          }else{
                                              id_detected = false;
                                          }
                                    }
                                }
                                stringBuilder.append("\n");

                            }
                            if(id_detected) {
                                Log.w("OCR", stringBuilder.toString());
                                parseID();
                                if (checkId_valid()) {
                                    textRecognizer.release();

                                    cameraPreview.captureImage();
                                    return;
                                }
                            }
                        }
                    });
                        ocr_process.start();



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

        Log.v("listitem","item list: " +item_list.toString());
        for(int index = 0;index<item_list.size();index++) {
            if (item_list.get(index).contains("TC")) {
                if (id_instance.getID() == null) {
                    if (index + 1 <= item_list.size() - 1) {
                        if (isNumeric(item_list.get(index + 1))) {
                            validity_count++;
                            id_instance.setID(Long.parseLong(item_list.get(index + 1)));

                            final CheckBox id_check = activity.findViewById(R.id.id_check);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    id_check.setChecked(true);
                                }
                            });
                        }
                    }
                }
            }

             if (item_list.get(index).contains("Soy") || item_list.get(index).contains("SOY")||  item_list.get(index).contains("soy") ||  item_list.get(index).contains("Sur") ||  item_list.get(index).contains("SUR") ||  item_list.get(index).contains("sur") ) {

                    if (id_instance.getSURNAME() == null) {
                        if (index + 1 <= item_list.size() - 1) {
                            validity_count++;
                            id_instance.setSURNAME(item_list.get(index + 1));
                            final CheckBox sur_check = activity.findViewById(R.id.surname_check);
                            activity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    sur_check.setChecked(true);
                                }
                            });

                        }
                    }
            }
            if (/*item_list.get(index).contains("Ad") ||  item_list.get(index).contains("ad") || */item_list.get(index).contains("Giv") || item_list.get(index).contains("GIV") || item_list.get(index).contains("giv") || item_list.get(index).contains("(s)") || item_list.get(index).contains("e(s)") || item_list.get(index).contains("Nam")){
                if (id_instance.getNAME() == null) {
                    if (index + 1 <= item_list.size() - 1) {
                        validity_count++;
                        id_instance.setNAME(item_list.get(index + 1));
                        final CheckBox name_check = activity.findViewById(R.id.name_check);
                        activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                name_check.setChecked(true);
                            }
                        });
                    }
                }
            }


                if (item_list.get(index).contains("Dog") || item_list.get(index).contains("dog") || item_list.get(index).contains("DOG") || item_list.get(index).contains("date") || item_list.get(index).contains("Date")) {
                    if (id_instance.getDATE_OF_BIRTH() == null) {
                        if (index + 1 <= item_list.size() - 1) {
                            validity_count++;
                            id_instance.setDATE_OF_BIRTH(item_list.get(index + 1));
                            final CheckBox dob_check = activity.findViewById(R.id.dob_check);
                            activity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    dob_check.setChecked(true);
                                }
                            });
                        }
                    }
                }

                if (item_list.get(index).equals("Seri No/")) {
                    if (id_instance.getSERIAL_NO() == null)
                        if (index + 1 <= item_list.size() - 1) {
                            id_instance.setSERIAL_NO(item_list.get(index + 1));
                        }
                }

                if (item_list.get(index).equals("Son Gecerlilik/ Valid Until")) {
                    if (id_instance.getVALID_UNTIL() == null)
                        if (index + 1 <= item_list.size() - 1) {
                            id_instance.setVALID_UNTIL(item_list.get(index + 1));
                        }
                }

                if (item_list.get(index).equals("Uyrugu/Nationality")) {
                    if (id_instance.getNATIONALITY() == null)
                        if (index + 1 <= item_list.size() - 1) {
                            id_instance.setNATIONALITY(item_list.get(index + 1));
                        }
                }

            }


    }

    public boolean checkId_valid(){
        Log.w("Validity Count",validity_count+"");
        if(validity_count == 4){
            final ImageView back = activity.findViewById(R.id.icon_id_back);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    back.setImageResource(R.drawable.icon_id_back_fin);
                }
            });
            return true;
        }
        return false;
    }

    public void start_ocr(){
        if(validity_count < 4){
            ocr_process.start();
        }
    }

    public void stop_ocr(){
        if(validity_count == 4){
            ocr_process.stop();
            ocr_process.destroy();
        }
        ocr_process.stop();
    }

}
