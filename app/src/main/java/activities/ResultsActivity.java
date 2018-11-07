package activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.murat.m_onboarding.R;

import java.io.FileNotFoundException;

import utils.CanvasView;
import utils.OCR;

public class ResultsActivity extends AppCompatActivity {
    private TextView tcknview;
    private Button restart;
    private ImageView signImage;
    private Bitmap signBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        restart =   findViewById(R.id.restart);
        signImage = findViewById(R.id.signImage);

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rstart = new Intent(ResultsActivity.this,SplashActivity.class);
                startActivity(rstart);
                finish();
            }
        });

        fillFields();


    }

    public void fillFields(){
        if(OCR.id_n != null){
            long tckn= OCR.id_n;
            tcknview= findViewById(R.id.tckn);
            tcknview.setText("TC: "+tckn);
        }

        try {
            signBitmap = BitmapFactory.decodeStream(getBaseContext().openFileInput("myImage"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        signImage.setImageBitmap(signBitmap);




    }
}
