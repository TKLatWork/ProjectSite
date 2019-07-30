package my.site.common.model.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        description = "通用的返回格式"
)
public class ApiResult<T> {
    public static final Integer STATUS_OK = 0;
    public static final Integer STATUS_API_ERROR = -1;
    public static final Integer STATUS_SYS_ERROR = -2;
    @ApiModelProperty(
            notes = "状态代码，成功:0, 业务失败:-1，系统错误:-2。其他自定。"
    )
    private Integer status;
    private String message;
    private T result;

    public ApiResult() {
        this.result = null;
    }

    public ApiResult(Integer status, String message, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public ApiResult(Integer status, T result) {
        this.status = status;
        this.message = status.toString();
        this.result = result;
    }

    public ApiResult(T result) {
        this(STATUS_OK, (String)null, result);
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
