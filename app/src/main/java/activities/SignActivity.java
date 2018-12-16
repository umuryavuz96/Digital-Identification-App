package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.murat.m_onboarding.R;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import utils.CanvasView;
import utils.CompareFaces;
import utils.FaceDetectAndCrop;
import utils.OCR;

public class SignActivity extends AppCompatActivity {

    private CanvasView canvasView;
    private Button nextButton4;
    public static Context context;
    private ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        canvasView= findViewById(R.id.canvasView);
        nextButton4=findViewById(R.id.nextButton4);


        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();



        context = this;

        nextButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDoalog = new ProgressDialog(context);
                progressDoalog.setMessage("Please wait ...");
                progressDoalog.setTitle("Comparing Results");
                progressDoalog.show();
                progressDoalog.setCancelable(false);

            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(SignActivity.this);
        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        builder.setMessage("Telefonunuzu yan çeviriniz ve kimliğinizde bulunan imzanın aynısını ekrana çiziniz.")
                .setTitle("İmza Onay");

        AlertDialog dialog = builder.create();
        builder.show();


    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public void clearCanvas(View v){
        canvasView.clearCanvas();
    }


    class AsyncTaskRunner extends AsyncTask<String, Void, Boolean> {
        @Override
        public Boolean doInBackground(String ... input){
            byte[] source = getByteArrays(OCR.face);
            byte[] target = getByteArrays(FaceDetectAndCrop.face_img);


                try {
                    CompareFaces compareFaces = new CompareFaces(source, target, context);

                } catch (Exception e) {
                    Log.e("Compare Faces", "Failed to initialize compare faces" + "\n error : " + e.toString());
                }



            return true;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            Intent goToResults = new Intent(context, ResultsActivity.class);
            Bitmap b =getBitmapFromView(canvasView);
            createImageFromBitmap(b);
            startActivity(goToResults);
            finish();
            progressDoalog.dismiss();

        }

        public byte[] getByteArrays(Bitmap img){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            return byteArray;
        }

    }
}
