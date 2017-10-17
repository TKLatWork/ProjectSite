package my.web.site.siteFrontend.cms.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CmsServiceException extends RuntimeException {

    public CmsServiceException(String message){
        super(message);
    }

}
