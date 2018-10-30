package activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.murat.m_onboarding.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread splash = new Thread() {
            @Override
            public void run() {
                try {
                    Intent goToInstructions = new Intent(SplashActivity.this,InstructionsActivity.class);
                    sleep(2500);
                    startActivity(goToInstructions);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        splash.start();

    }
}
