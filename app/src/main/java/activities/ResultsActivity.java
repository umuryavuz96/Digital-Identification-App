package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.murat.m_onboarding.R;

import utils.OCR;

public class ResultsActivity extends AppCompatActivity {
    private TextView tcknview;
    private Button restart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        restart = (Button) findViewById(R.id.restart);

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rstart = new Intent(ResultsActivity.this,InstructionsActivity.class);
                startActivity(rstart);
                finish();
            }
        });

        if(OCR.id_n != null){
            long tckn= OCR.id_n;
            tcknview= (TextView) findViewById(R.id.tckn);
            tcknview.setText("TC: "+tckn);
        }
    }
}
