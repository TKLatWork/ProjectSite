package my.site.common.util;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class LogUtil {

    public static String getString(Object t){
        if(t == null){
            return "null";
        }else{
            return ToStringBuilder.reflectionToString(t);
        }
    }

}
