package my.web.site.commons.commons;

public class CommonResponseBuilder {
    private CommonsValues.ResponseStatus status;
    private String message = null;
    private Object attachment = null;
    private Object data = null;

    public CommonResponseBuilder setStatus(CommonsValues.ResponseStatus status) {
        this.status = status;
        return this;
    }

    public CommonResponseBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public CommonResponseBuilder setAttachment(Object attachment) {
        this.attachment = attachment;
        return this;
    }

    public CommonResponseBuilder setData(Object data) {
        this.data = data;
        return this;
    }

    public CommonResponse createCommonResponse() {
        return new CommonResponse(status, message, attachment, data);
    }
}