package org.jeecg.modules.upload.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 附件
 * @Author: jeecg-boot
 * @Date:   2021-06-02
 * @Version: V1.0
 */
@Data
@TableName("upload_file")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="upload_file对象", description="附件")
@NoArgsConstructor
public class UploadFile extends JeecgEntity {
	/**部门*/
	@Excel(name = "部门", width = 15)
	@ApiModelProperty(value = "部门")
	private String sysOrgCode;
	/**文件名称*/
	@Excel(name = "文件名称", width = 15)
    @ApiModelProperty(value = "文件名称")
	private String fileName;
	/**文件别名*/
	@Excel(name = "文件别名", width = 15)
	@ApiModelProperty(value = "文件别名")
	private String fileAlias;
	/**文件路径*/
	@Excel(name = "文件路径", width = 15)
    @ApiModelProperty(value = "文件路径")
	private String filePath;
	/**文件类型，字典由具体业务决定*/
	@Excel(name = "文件类型", width = 15)
	@ApiModelProperty(value = "文件类型")
	private String fileType;
	/**业务主键Id*/
	@Excel(name = "业务主键Id", width = 15)
    @ApiModelProperty(value = "业务主键Id")
	private String objectId;
	/**业务类型，默认表名*/
	@Excel(name = "业务类型", width = 15)
	@ApiModelProperty(value = "业务类型")
	private String businessType;
	/**字典编码*/
	@Excel(name = "字典编码", width = 15)
	@ApiModelProperty(value = "字典编码")
	@TableField(exist = false)
	private String dictCode;
	/**字典项名*/
	@Excel(name = "字典项名", width = 15)
	@ApiModelProperty(value = "字典项名")
	@TableField(exist = false)
	private String fileTypeName;

}
