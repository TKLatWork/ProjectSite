package my.site.account.util;

import com.google.gson.Gson;
import my.site.common.model.api.ApiException;
import my.site.common.model.api.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static my.site.common.model.api.ApiResult.STATUS_API_ERROR;
import static my.site.common.model.api.ApiResult.STATUS_SYS_ERROR;

/**
 * 全局错误处理
 * ApiException 200
 * 其他 500
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        String message = ex.getMessage();
        //默认status
        Integer status = STATUS_SYS_ERROR;
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        //处理业务错误
        if(ex instanceof ApiException){
            LOG.info(ex.getMessage());
            httpStatus = HttpStatus.OK;
            ApiException apiException = (ApiException)ex;
            if(apiException.getCode() != null){
                status = apiException.getCode();
            }else{
                status = STATUS_API_ERROR;
            }
        }else{
            LOG.error(ex.getMessage(), ex);
        }
        //产生Json回复
        String responseJson = new Gson().toJson(new ApiResult<>(status, message, null));
        return handleExceptionInternal(ex, responseJson, new HttpHeaders(), httpStatus, request);
    }

}
