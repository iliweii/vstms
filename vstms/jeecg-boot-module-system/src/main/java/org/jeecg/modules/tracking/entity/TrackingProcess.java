package org.jeecg.modules.tracking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 训后跟踪过程
 * @Author: jeecg-boot
 * @Date:   2022-05-31
 * @Version: V1.0
 */
@Data
@TableName("tracking_process")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tracking_process对象", description="训后跟踪过程")
public class TrackingProcess extends JeecgEntity {

	/**跟踪ID*/
	@Excel(name = "跟踪ID", width = 15)
    @ApiModelProperty(value = "跟踪ID")
	private java.lang.String reqId;
	/**过程类型*/
	@Excel(name = "过程类型", width = 15)
    @ApiModelProperty(value = "过程类型")
	private java.lang.String type;
	/**附件路径*/
	@Excel(name = "附件路径", width = 15)
    @ApiModelProperty(value = "附件路径")
	private java.lang.String filePath;
	/**跟踪内容*/
	@Excel(name = "跟踪内容", width = 15)
    @ApiModelProperty(value = "跟踪内容")
	private java.lang.String remarks;

}
