package utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;

import java.nio.ByteBuffer;

public class ID_Validity extends AsyncTask<byte[], Void, Boolean>{

    private Context context;
    private Activity activity;
    public static boolean valid = false;
    private Float confidence_treshold = 85f;
    private ProgressDialog progressDoalog;


    public ID_Validity(Context context, Activity activity) {
        this.context = context;
        this.activity =activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Please wait ...");
        progressDoalog.setTitle("Checking ID validity");
        progressDoalog.show();
        progressDoalog.setCancelable(false);
    }

    @Override
    protected Boolean doInBackground(byte[]... source) {

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-2:86cb49aa-8f3f-49a2-a4af-78b3db0a26cc", // Identity pool ID
                Regions.US_EAST_2 // Region
        );

        AmazonRekognition rekognitionClient = new AmazonRekognitionClient(credentialsProvider);

        ByteBuffer img_buffer = ByteBuffer.wrap(source[0]);

        Image source_image =new Image()
                .withBytes(img_buffer);

        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(source_image)
                .withMinConfidence(confidence_treshold);

        DetectLabelsResult result = rekognitionClient.detectLabels(request);

        Log.d("ID_VALIDITY","Results: \n");
        for(Label label: result.getLabels()){
            Log.d("ID_VALIDITY",label.getName() + " = " + label.getConfidence());

            if(label.getName().equals("Id Cards")){
                valid = true;
            }
        }

        return true;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDoalog.dismiss();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if(ID_Validity.valid) {
                    Toast.makeText(context,"ID is valid",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"ID is not valid",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
