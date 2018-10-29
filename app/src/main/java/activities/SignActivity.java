package activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog.Builder;

import com.example.murat.m_onboarding.R;

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
                startActivity(goToResults);

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

    public void clearCanvas(View v){
        canvasView.clearCanvas();
    }


}
