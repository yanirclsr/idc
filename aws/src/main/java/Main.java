import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.boot.SpringApplication;

public class Main {

    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
        System.out.println("ssa");
        AWSCredentials credentials;
        try {
            credentials = new BasicAWSCredentials("XXXXXXXXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        } catch (Exception e){

        }
    }
}
