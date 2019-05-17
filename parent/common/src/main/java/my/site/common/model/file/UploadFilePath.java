package my.site.common.model.file;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.UnsupportedEncodingException;

import static my.site.common.util.EncodeUtil.urlEncode;

/**
 * 基本的文件路径约束
 */
@Data
public class UploadFilePath {

    private FileType fileType;
    @ApiModelProperty("相关连的主要对象id")
    private String relativeId;
    @ApiModelProperty("文件名称")
    private String name;

    public String getPath() throws UnsupportedEncodingException {
        return "/" + urlEncode(fileType.name())
                + "/" + urlEncode(relativeId)
                + "/" + urlEncode(name);
    }

}
