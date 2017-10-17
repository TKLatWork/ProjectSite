package my.web.site.siteFrontend.commons;

import my.web.site.commons.commons.CommonResponse;
import my.web.site.commons.commons.CommonResponseBuilder;
import my.web.site.commons.commons.CommonsValues;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@ControllerAdvice
public class CtrlExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public CommonResponse onException(HttpServletResponse response, Exception ex) {
//        //TODO 501能带body？
//        response.setContentType("application/json");
//
//        CommonResponse data = new CommonResponseBuilder().createCommonResponse();
//        data.setStatus(CommonsValues.ResponseStatus.Exception);
//        data.setMessage(ex.getMessage());
//        data.setAttachment(ExceptionUtils.getStackTrace(ex));
//        return data;
//    }

}
