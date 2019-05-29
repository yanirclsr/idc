package com.idc.speak;

import com.amazonaws.services.polly.model.LanguageCode;

public class Speak {

    public static void main(String[] args) {

        LanguageCode srcLang= LanguageCode.EnUS;
        LanguageCode targetLang = LanguageCode.EsUS;

        String SAMPLE = "Congratulations. You have successfully built this working demo of Amazon Polly in Java. Have fun building voice enabled apps with Amazon Polly (that's me!), and always look at the AWS website for tips and tricks on using Amazon Polly and other great services from AWS";
//
//        SpeakActions actions = new SpeakActions();
//        String txt = actions.translate(SAMPLE,
//                srcLang,
//                targetLang);
//
//        actions.speak(txt, targetLang);

    }

}
