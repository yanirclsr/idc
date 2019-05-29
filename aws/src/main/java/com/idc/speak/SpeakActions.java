package com.idc.speak;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.*;
import com.amazonaws.services.transcribe.AmazonTranscribe;
import com.amazonaws.services.transcribe.AmazonTranscribeClientBuilder;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClientBuilder;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import com.idc.image.utils.AwsAuth;

import java.io.InputStream;

public class SpeakActions {


    private Regions REGION = Regions.US_WEST_2;
    private OutputFormat FORMAT = OutputFormat.Mp3;

    public String speach2Text(){

        AmazonTranscribe transcribe = AmazonTranscribeClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new AwsAuth().get()))
                .withRegion(REGION)
                .build();



        return "";
    }

    public String translate(String txt, LanguageCode srcLang, String targetLang){

        String sLang = srcLang.toString().split("-")[0];
        String tLang = targetLang.split("-")[0];

        AmazonTranslate translate = AmazonTranslateClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new AwsAuth().get()))
                .withRegion(REGION)
                .build();

        TranslateTextRequest request = new TranslateTextRequest()
                .withText(txt)
                .withSourceLanguageCode(sLang.toString())
                .withTargetLanguageCode(tLang);
        TranslateTextResult result  = translate.translateText(request);
        String resultTxt = result.getTranslatedText();
        System.out.println(resultTxt);

        return resultTxt;


    }

    public InputStream speak(String TXT, LanguageCode lang){

        try {

            //create the test class
            AmazonPolly polly = AmazonPollyClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(new AwsAuth().get()))
                    .withRegion(REGION)
                    .build();

            DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();

            describeVoicesRequest.setLanguageCode(lang);

            // Synchronously ask Amazon Polly to describe available TTS voices.
            DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
            Voice voice = describeVoicesResult.getVoices().get(0);

            //get the audio stream
            SynthesizeSpeechRequest synthReq =
                    new SynthesizeSpeechRequest().withText(TXT).withVoiceId(voice.getId())
                            .withOutputFormat(FORMAT);

            SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);

            InputStream speechStream =  synthRes.getAudioStream();

//            //create an MP3 player
//            AdvancedPlayer player = new AdvancedPlayer(speechStream,
//                    javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());
//
//            player.setPlayBackListener(new PlaybackListener() {
//                @Override
//                public void playbackStarted(PlaybackEvent evt) {
//                    System.out.println("Playback started");
//                    System.out.println(TXT);
//                }
//
//                @Override
//                public void playbackFinished(PlaybackEvent evt) {
//                    System.out.println("Playback finished");
//                }
//            });


            // play it!
//            player.play();

            return speechStream;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
//
//    public void PollyDemo() {
////        // create an Amazon Polly client in a specific region
////        AmazonPollyClient polly = new AmazonPollyClient(new DefaultAWSCredentialsProviderChain(),
////                new ClientConfiguration());
////        polly.setRegion(Region.getRegion(REGION));
////        // Create describe voices request.
////        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();
////
////        // Synchronously ask Amazon Polly to describe available TTS voices.
////        DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
////        voice = describeVoicesResult.getVoices().get(0);
//    }
//
//    public InputStream synthesize(String text, OutputFormat format) throws IOException {
//        SynthesizeSpeechRequest synthReq =
//                new SynthesizeSpeechRequest().withText(text).withVoiceId(voice.getId())
//                        .withOutputFormat(format);
//        SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);
//
//        return synthRes.getAudioStream();
//    }

}
