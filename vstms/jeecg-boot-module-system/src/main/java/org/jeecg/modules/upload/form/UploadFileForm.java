package org.jeecg.modules.upload.form;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 附件
 * @Author: jeecg-boot
 * @Date:   2021-06-02
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@ApiModel(value="upload_file表单", description="附件")
public class UploadFileForm {
    @ApiModelProperty(value = "文件名称")
	private String name;
    @ApiModelProperty(value = "文件路径")
	private String uId;
}
