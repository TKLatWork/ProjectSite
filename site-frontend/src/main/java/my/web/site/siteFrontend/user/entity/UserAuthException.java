package my.web.site.siteFrontend.user.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAuthException extends RuntimeException {

    public UserAuthException(String message){
        super(message);
    }

}
