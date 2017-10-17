package my.web.site.commons.commons;

import my.web.site.commons.cms.model.Record;

public class CommonResponse {

    public CommonResponse(){};

    public CommonResponse(CommonsValues.ResponseStatus status, String message){
        this(status, message, null ,null);
    }

    public CommonResponse(CommonsValues.ResponseStatus status, String message, Object attachment, Object data){
        this.status = status;
        this.message = message;
        this.attachment = attachment;
        this.data = data;
    }

    private CommonsValues.ResponseStatus status;
    private String message;
    private Object attachment;
    private Object data;

    public CommonsValues.ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(CommonsValues.ResponseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
