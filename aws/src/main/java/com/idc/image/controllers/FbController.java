package com.idc.image.controllers;

import com.idc.image.actions.AwsActions;
import com.idc.image.actions.FbActions;
import com.idc.image.entities.FbImg;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FbController {

    private FbActions fbActions = new FbActions();
    private AwsActions awsActions = new AwsActions();
    private int MAX_LABELS = 15;
    private float MIN_CONF = 65L;

    @RequestMapping(value = "/analyze", method = RequestMethod.POST)
    public @ResponseBody
    String analyzeImgs(@RequestBody String fbImgs){

        System.out.println(fbImgs);

        List<FbImg> imgs = fbActions.readImgs(fbImgs);
        System.out.println(imgs);

        awsActions.bulkDetectLabels(imgs,MAX_LABELS, MIN_CONF);
        System.out.println(imgs);


        return fbImgs.toString();

    }
}
