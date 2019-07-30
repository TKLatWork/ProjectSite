package my.site.common.model.api;

import lombok.Data;

@Data
public class ApiException extends Exception {

    private Integer code;

    public ApiException(String message, Integer code){
        super(message);
        this.code = code;
    }

    public ApiException(String message){
        super(message);
        this.code = ApiResult.STATUS_API_ERROR;
    }

}
