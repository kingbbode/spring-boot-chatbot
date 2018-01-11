package com.github.kingbbode.messenger.teamup.util;

import com.github.kingbbode.messenger.teamup.response.MessageResponse;
import org.springframework.http.MediaType;

/**
 * Created by YG on 2017-05-17.
 */
public class ImagesUtils {
    private static final String IMAGE_PATTERN_REGEX = "([^(\\s|\\.)]+(\\.(?i)(jpg|png))$)";
    
    public static boolean isImage(MessageResponse.File file) {
        return file.getName() != null && file.getName().matches(IMAGE_PATTERN_REGEX);
    }

    public static MediaType selectMediaTypeForImage(String fileName) {
        if("jpg".equals(fileName)){
            return MediaType.IMAGE_JPEG;
        }else if("png".equals(fileName)){
            return MediaType.IMAGE_PNG;
        }
        throw new NullPointerException();
    }
}
