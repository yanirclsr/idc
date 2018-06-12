package actions;

import entities.PersonImg;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import utils.Rest;

import java.util.ArrayList;
import java.util.List;

public class FbiActions {


    private Rest rest = new Rest();

//    public List
//    https://api.fbi.gov/wanted/v1/list?page=3
//    public List<>
    public List<PersonImg> getAllWanteds(){

        AwsActions awsActions = new AwsActions();
        awsActions.AS3Invoker();
        List<PersonImg> list = new ArrayList<>();
        String url = "https://api.fbi.gov/wanted/v1/list?page=";

//        int total = Integer.parseInt(json.get("total").toString());
        int runs = (int) Math.ceil(717/20);

        for(int a = 0 ; a < runs; a++) {

            JSONObject json = rest.GET(url + a);
//            int items = Integer.parseInt(json.get("total").toString());

            JSONArray arr = (JSONArray) JSONValue.parse(json.get("items").toString());
            for (int b = 0; b < arr.size(); b++) {
                JSONObject obj = (JSONObject) arr.get(b);
                String uid = obj.get("uid").toString();
                JSONArray imgArr = (JSONArray) obj.get("images");
                System.out.println("found " + imgArr.size() + " images for this person");

                for (int c = 0; c < imgArr.size(); c++) {
                    PersonImg personImg = new PersonImg();
                    JSONObject imgJSON = (JSONObject) imgArr.get(c);
                    String name = uid + "--" + c;
                    String u = imgJSON.get("original").toString();
//                    personImg.setId(uid);
//                    personImg.setUrlrls(imgJSON.get("original").toString());
                    if(u.endsWith(".jpg")) {
                        System.out.println(uid + " > " + u);
                        list.add(personImg);
                        awsActions.uploadToS3(name, u);
                    }
                }

            }

        }
        return list;


    }

}
