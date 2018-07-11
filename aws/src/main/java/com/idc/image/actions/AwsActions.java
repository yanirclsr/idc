package com.idc.image.actions;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idc.image.entities.FbImg;
import com.idc.image.utils.AwsAuth;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AwsActions {

    private Regions REGION = Regions.US_WEST_2;
    private String BUCKET_NAME = "idcuswanted";
    private String KEY_NAME = "123";
    private AmazonRekognition ar;
    private AmazonS3 s3;
    private List<String> humanLbls = new ArrayList<>(Arrays.asList("Person","People","Human"));

    public AwsActions AS3Invoker(){
        this.s3 = AmazonS3ClientBuilder.standard().withRegion(REGION)
                .withCredentials(new AWSStaticCredentialsProvider(new AwsAuth().get())).build();
        return this;
    }

    public void uploadToS3(String name, String url){
        if(s3 == null) AS3Invoker();
        try {
//            s3.putObject(BUCKET_NAME, name, urlToFile(url));
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
    }

    public AmazonRekognition FRInvoker() {

        System.out.println("running FRInvoker");

        AWSCredentials creds = new AwsAuth().get();

        if (creds != null) {
            this.ar = AmazonRekognitionClientBuilder.standard().withRegion(REGION)
                    .withCredentials(new AWSStaticCredentialsProvider(creds)).build();

            return ar;

        }else return null;

    }

    public List<FbImg> bulkDetectLabels(List<FbImg> imgs, int MAX_LABELS, float MIN_CONF){
        for(FbImg img: imgs){
            List<Label> labels = this.detectLabels(img.getImg(), MAX_LABELS, MIN_CONF);
            img.setLabels(labels);
            if(checkIfPeople(labels)) this.detectFaceDetails(img.getImg());
        }
        return imgs;
    }

    public boolean checkIfPeople(List<Label> lbls){

        for(Label lbl: lbls){
            if(humanLbls.contains(lbl.getName())) return true;
        }
        return false;
    }

//    public List<Label> detectLabels(String imgURL, int MAX_LABELS, float MIN_CONF){
////        ByteBuffer img = imgUrlToByte(imgURL);
////        return detectLabels(img,MAX_LABELS, MIN_CONF);
//    }

    public List<Label> detectLabels(ByteBuffer img, int MAX_LABELS, float MIN_CONF){

        try {
            if(ar == null) FRInvoker();
            DetectLabelsRequest dlr = new DetectLabelsRequest()
                    .withImage(new Image().withBytes(img))
                    .withMaxLabels(MAX_LABELS)
                    .withMinConfidence(MIN_CONF);

            DetectLabelsResult results = this.ar.detectLabels(dlr);
            List<Label> labels = results.getLabels();
            System.out.println("found " + labels + " labels");
            return labels;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public List<Label> detectFaceDetails(ByteBuffer img) {
        if (ar == null) FRInvoker();
        DetectFacesRequest request = new DetectFacesRequest()
                .withImage(new Image().withBytes(img))
                .withAttributes(Attribute.ALL);

        try {
            DetectFacesResult result = ar.detectFaces(request);
            List<FaceDetail> faceDetails = result.getFaceDetails();

            for (FaceDetail face : faceDetails) {
                if (request.getAttributes().contains("ALL")) {
                    AgeRange ageRange = face.getAgeRange();
                    System.out.println("The detected face is estimated to be between "
                            + ageRange.getLow().toString() + " and " + ageRange.getHigh().toString()
                            + " years old.");
                    System.out.println("Here's the complete set of attributes:");
                } else { // non-default attributes have null values.
                    System.out.println("Here's the default set of attributes:");
                }

                ObjectMapper objectMapper = new ObjectMapper();
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
            }

        } catch (AmazonRekognitionException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }


}
