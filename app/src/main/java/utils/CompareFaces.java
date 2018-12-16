package utils;

//Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
//PDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-rekognition-developer-guide/blob/master/LICENSE-SAMPLECODE.)


import android.content.Context;

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
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import com.amazonaws.util.IOUtils;

public class CompareFaces {

    public static String result;

    public CompareFaces(byte[] source_img,byte[] target_img,Context context) throws Exception{
        Float similarityThreshold = 0F;
        byte[] sourceImage = source_img;
        byte[] targetImage = target_img;

        ByteBuffer sourceImageBytes=ByteBuffer.wrap(sourceImage);
        ;
        ByteBuffer targetImageBytes=ByteBuffer.wrap(targetImage);
        ;

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
        CompareFacesResult compareFacesResult=rekognitionClient.compareFaces(request);

        // Display results
        List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
        for (CompareFacesMatch match: faceDetails){
            ComparedFace face= match.getFace();
            BoundingBox position = face.getBoundingBox();
            System.out.println("Face at " + position.getLeft().toString()
                    + " " + position.getTop()
                    + " matches with " + face.getConfidence().toString()
                    + "% confidence.");
            result = face.getConfidence().toString();
        }
        //List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();

        //System.out.println("There was " + uncompared.size() + " face(s) that did not match");
        //System.out.println("Source image rotation: " + compareFacesResult.getSourceImageOrientationCorrection());
        //System.out.println("target image rotation: " + compareFacesResult.getTargetImageOrientationCorrection());
    }
}
