package activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.murat.m_onboarding.R;

import utils.FaceDetectAndCrop;

public class TempImageClass extends AppCompatActivity {

    ImageView img;

    private FaceDetectAndCrop mFaceOverlayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_image);
        img = (ImageView) findViewById(R.id.img);

        mFaceOverlayView = new FaceDetectAndCrop(this,img);

        mFaceOverlayView.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.kimlik));














        //Bundle extras = getIntent().getExtras();

        //assert extras != null;

        //byte[] b = IDScanActivity.b;

        //Log.w("IMAGE",b.toString());

        //if(b!=null){
        //    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0,b.length);



            //img.setImageBitmap(bitmap);
        //}


    }
}
