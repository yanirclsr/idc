package com.idc.speak;


import com.amazonaws.services.polly.model.LanguageCode;
import com.amazonaws.util.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class SpeakController {

    @RequestMapping(value = "/api/translate.mp3")
    public @ResponseBody ResponseEntity<Resource> getTranslate(
            @RequestParam(value = "txt") String txtStr,
            @RequestParam(value = "toLang") String toLang,
            HttpServletResponse response) throws IOException {

        LanguageCode srcLang= LanguageCode.EnUS;

//        String SAMPLE = "Congratulations. You have successfully built this working demo of Amazon Polly in Java. Have fun building voice enabled apps with Amazon Polly (that's me!), and always look at the AWS website for tips and tricks on using Amazon Polly and other great services from AWS";

        SpeakActions actions = new SpeakActions();
        String txt = actions.translate(txtStr,
                srcLang,
                toLang);

        InputStream stream =  actions.speak(txt, LanguageCode.fromValue(toLang));

        // Set the content type and attachment header.
        response.addHeader("Content-disposition", "attachment;filename=11.mp3");
        response.setContentType("audio/mpeg");

        // Copy the stream to the response's output stream.
        IOUtils.copy(stream, response.getOutputStream());
        response.flushBuffer();

        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(new InputStreamResource(stream));
    }

}
