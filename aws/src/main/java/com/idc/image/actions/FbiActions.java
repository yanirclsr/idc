package com.idc.image.actions;

import com.idc.image.entities.PersonImg;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import com.idc.image.utils.Rest;

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

        for(int a = 25 ; a < runs; a++) {

            System.out.println("page #" + a);

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
                    if(u.endsWith(".jpg")) {
                        personImg.setId(name);
                        personImg.setUrlrls(u);
                        System.out.println(uid + " > " + u);
                        list.add(personImg);
                    }
//                    personImg.setUrlrls(imgJSON.get("original").toString());
                }

            }

        }
        return list;


    }

    public void getFbiImages(){
        System.out.println(System.getProperty("user.dir"));

        FbiActions fbiActions = new FbiActions();


        List<PersonImg> pplList = fbiActions.getAllWanteds();

        AwsActions awsActions = new AwsActions();
        awsActions.AS3Invoker();
        for(PersonImg prsn: pplList){
            if(! prsn.getId().contains("e3e4565e4eeffe4e4177119058ee867d")) awsActions.uploadToS3(prsn.getId(), prsn.getUrlrls());

        }
    }

}
