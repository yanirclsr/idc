package actions;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.util.IOUtils;
import utils.AwsAuth;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.List;

public class AwsActions {

    private Regions REGION = Regions.US_WEST_2;
    private String BUCKET_NAME = "idcuswanted";
    private String KEY_NAME = "123";
    private AmazonRekognition ar;
    private AmazonS3 s3;

    public AwsActions AS3Invoker(){
        this.s3 = AmazonS3ClientBuilder.standard().withRegion(REGION)
                .withCredentials(new AWSStaticCredentialsProvider(new AwsAuth().get())).build();
        return this;
    }

    public void uploadToS3(String name, String url){
        try {
            s3.putObject(BUCKET_NAME, name, urlToFile(url));
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
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

    public List<Label> detectLabels(String imgURL, int MAX_LABELS, float MIN_CONF){
        ByteBuffer img = imgUrlToByte(imgURL);
        return detectLabels(img,MAX_LABELS, MIN_CONF);
    }

    public List<Label> detectLabels(ByteBuffer img, int MAX_LABELS, float MIN_CONF){

        try {
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

    public ByteBuffer imgUrlToByte(String imgUrl){
        try {
            URL url = new URL(imgUrl);
            InputStream is = url.openStream();
            ByteBuffer bb = ByteBuffer.wrap(IOUtils.toByteArray(is));
            return bb;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ByteBuffer imgFileToByte(String imgPath){
        try {

            InputStream is = new FileInputStream(new File(imgPath));
            ByteBuffer bb = ByteBuffer.wrap(IOUtils.toByteArray(is));
            return bb;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static File urlToFile(String urlStr){
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
            BufferedImage img = ImageIO.read(connection.getInputStream());


//            BufferedImage img = ImageIO.read(url);
            File file = new File("downloaded.jpg");
            ImageIO.write(img, "jpg", file);
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
