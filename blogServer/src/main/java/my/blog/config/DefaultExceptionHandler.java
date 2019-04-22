package my.blog.config;

import lombok.extern.slf4j.Slf4j;
import my.blog.model.ApiException;
import my.blog.model.Const;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiException sessionNotFoundExceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
        log.error(request.getRequestURI() + ":" + exception.getMessage(), exception);
        return new ApiException(exception.getMessage(), Const.ApiStatus.ERROR);
    }

}
