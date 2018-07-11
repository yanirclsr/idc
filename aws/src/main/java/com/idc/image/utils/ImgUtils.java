package com.idc.image.utils;

import com.amazonaws.util.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

public class ImgUtils {

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
