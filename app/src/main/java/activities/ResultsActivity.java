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

import models.ID;
import utils.CanvasView;
import utils.OCR;

public class ResultsActivity extends AppCompatActivity {
    private TextView tckn_text;
    private TextView name;
    private TextView surname;
    private TextView date_of_birth;




    private Button restart;
    private ImageView signImage;
    private Bitmap signBitmap;

    private ID id_instace;

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
        if(OCR.id_instance != null){
            id_instace = OCR.id_instance;

            tckn_text = (TextView) findViewById(R.id.tckn);
            name = (TextView) findViewById(R.id.name);
            surname = (TextView) findViewById(R.id.surname);
            date_of_birth = (TextView) findViewById(R.id.date_of_birth);

            tckn_text.setText(id_instace.getID()+"");
            name.setText(id_instace.getNAME());
            surname.setText(id_instace.getSURNAME());
            date_of_birth.setText(id_instace.getDATE_OF_BIRTH());
        }

        try {
            signBitmap = BitmapFactory.decodeStream(getBaseContext().openFileInput("myImage"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        signImage.setImageBitmap(signBitmap);




    }
}
