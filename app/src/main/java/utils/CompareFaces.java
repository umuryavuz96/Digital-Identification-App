package utils;

//Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
//PDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-rekognition-developer-guide/blob/master/LICENSE-SAMPLECODE.)


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;

import java.text.DecimalFormat;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import com.amazonaws.util.IOUtils;

import activities.ResultsActivity;

public class CompareFaces extends AsyncTask<byte[], Void, Boolean> {

    public static String result;
    private Context context;
    private Activity activity;

    private ProgressDialog progressDoalog;

    public CompareFaces(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Please wait ...");
        progressDoalog.setTitle("Comparing Results");
        progressDoalog.show();
        progressDoalog.setCancelable(false);

    }


    @Override
    protected Boolean doInBackground(byte[]... bytes) {
        Float similarityThreshold = 0F;
        byte[] sourceImage = bytes[0];
        byte[] targetImage = bytes[1];

        ByteBuffer sourceImageBytes =ByteBuffer.wrap(sourceImage);

        ByteBuffer targetImageBytes =ByteBuffer.wrap(targetImage);


        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-2:86cb49aa-8f3f-49a2-a4af-78b3db0a26cc", // Identity pool ID
                Regions.US_EAST_2 // Region
        );

        AmazonRekognition rekognitionClient = new AmazonRekognitionClient(credentialsProvider);

        Image source=new Image()
                .withBytes(sourceImageBytes);
        Image target=new Image()
                .withBytes(targetImageBytes);

        CompareFacesRequest request = new CompareFacesRequest()
                .withSourceImage(source)
                .withTargetImage(target)
                .withSimilarityThreshold(similarityThreshold);

        // Call operation
        CompareFacesResult compareFacesResult = rekognitionClient.compareFaces(request);

        // Display results

        List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
        ComparedFace face= faceDetails.get(0).getFace();
        BoundingBox position = face.getBoundingBox();
        System.out.println("Face at " + position.getLeft().toString()
                + " " + position.getTop()
                + " matches with " + faceDetails.get(0).getSimilarity()
                + "% similarity.");

        DecimalFormat df = new DecimalFormat("##.#");
        result = df.format(faceDetails.get(0).getSimilarity());



        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        Intent goToResults = new Intent(context, ResultsActivity.class);

        activity.startActivity(goToResults);
        activity.finish();
        progressDoalog.dismiss();

    }
}
