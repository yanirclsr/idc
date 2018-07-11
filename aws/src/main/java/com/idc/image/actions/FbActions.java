package com.idc.image.actions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idc.image.entities.FbImg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FbActions {

    public List<FbImg> readImgs(String imgs){
        List<FbImg> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            list = mapper.readValue(imgs,
                    new TypeReference<ArrayList<FbImg>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


}
