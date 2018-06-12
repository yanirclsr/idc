import actions.AwsActions;
import actions.FbiActions;
import org.springframework.boot.SpringApplication;

public class Main {

    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
        System.out.println(System.getProperty("user.dir"));

        AwsActions awsActions = new AwsActions();
        FbiActions fbiActions = new FbiActions();


        //https://api.fbi.gov/wanted/v1/list?page=3

        fbiActions.getAllWanteds();

        for(int i =0 ; i < 100; i++){
//            fbiActions
        }

//        actions.FRInvoker();
//        actions.detectLabels("https://pbs.twimg.com/profile_images/476951903499063296/qc58njjp.jpeg",10,50L);

    }
}
