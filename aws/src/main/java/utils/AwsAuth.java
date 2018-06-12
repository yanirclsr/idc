package utils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

public class AwsAuth {

    private String accessKey = "AKIAITU6SFKNITSBQBFQ";
    private String secretKey = "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";

    public AWSCredentials get(){
        System.out.println("rgetting AWSCredentials");

        if (accessKey == null || secretKey == null) return null;
        AWSCredentials credentials;
        try {
            credentials = new BasicAWSCredentials(accessKey, secretKey);
        } catch (Exception e){
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (/Users/userid/.aws/credentials), and is in a valid format.", e);
        }
        return credentials;
    }

}
