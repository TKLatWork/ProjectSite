package my.site.common.model.file;

import lombok.Data;

/**
 * 代表一个上传的文件
 */
@Data
public class FileRecord {

    private UploadFilePath filePath;
    private String name;
    private String md5;
    private Long size;

}
