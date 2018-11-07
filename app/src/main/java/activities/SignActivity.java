package activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog.Builder;

import com.example.murat.m_onboarding.R;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import utils.CanvasView;

public class SignActivity extends AppCompatActivity {

    private CanvasView canvasView;
    private Button nextButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        canvasView= findViewById(R.id.canvasView);
        nextButton4=findViewById(R.id.nextButton4);

        nextButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToResults = new Intent(getBaseContext(), ResultsActivity.class);
                Bitmap b =getBitmapFromView(canvasView);
                createImageFromBitmap(b);
                startActivity(goToResults);
                finish();

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


}
