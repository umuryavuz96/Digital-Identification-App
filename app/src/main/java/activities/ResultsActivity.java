package activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.murat.m_onboarding.R;

import utils.OCR;

public class ResultsActivity extends AppCompatActivity {
    private TextView tcknview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

       long tckn= OCR.id_n;

        tcknview= findViewById(R.id.tckn);
        tcknview.setText("TC: "+tckn);
    }
}
