package my.web.site.commons.cms.util;


import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class CmsUtil {

    public static String genFileName(String orgFileName){
        String name = StringUtils.left(FilenameUtils.getBaseName(orgFileName), 10) + "-" + UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(orgFileName);
        return name;
    }

}
