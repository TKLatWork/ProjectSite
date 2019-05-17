package my.site.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class EncodeUtil {

    public static String urlEncode(String org) throws UnsupportedEncodingException {
        return URLEncoder.encode(org, StandardCharsets.UTF_8.name());
    }

}
