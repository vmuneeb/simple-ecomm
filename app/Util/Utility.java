package Util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by muneeb on 27/01/17.
 */

public class Utility {

    private static final Logger LOG = LoggerFactory.getLogger(Utility.class);

    public static String getFormattedPrice(double price) {
        if(price < 100) {
            return Double.toString(price);
        }
        return Double.toString(price);
    }

    public static boolean checkImage(Http.MultipartFormData.FilePart<File> image) {

        if(image == null) {
            LOG.info("Image is null");
            return false;
        }
        File imageFile = image.getFile();

        if( imageFile.length() ==0) {
            return false;
        }

        try {
            String fileType = Files.probeContentType(imageFile.toPath());

            if (fileType!= null && !fileType.equalsIgnoreCase("image/jpeg") && !fileType.equalsIgnoreCase("image/png") && !fileType.equalsIgnoreCase("image/jpg")) {
                return false;
            }

        } catch (IOException ex) {
            return false;
        }
        return true;
    }

}
