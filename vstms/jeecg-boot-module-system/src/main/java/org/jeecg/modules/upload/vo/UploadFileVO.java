package org.jeecg.modules.upload.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.upload.entity.UploadFile;

import java.util.List;

@Data
public class UploadFileVO extends UploadFile {

    /**文件List*/
    @ApiModelProperty(value = "文件List")
    private List<UploadFile> file;

    /**
     * 文件大小
     */
    private Long fileSize;

}
