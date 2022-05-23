package org.jeecg.modules.research.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 需求调研过程
 * @Author: jeecg-boot
 * @Date:   2022-05-23
 * @Version: V1.0
 */
@Data
@TableName("research_process")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="research_process对象", description="需求调研过程")
public class ResearchProcess extends JeecgEntity {

	/**需求ID*/
	@Excel(name = "需求ID", width = 15)
    @ApiModelProperty(value = "需求ID")
	private java.lang.String reqId;
	/**过程类型*/
	@Excel(name = "过程类型", width = 15)
    @ApiModelProperty(value = "过程类型")
	private java.lang.String type;
	/**附件路径*/
	@Excel(name = "附件路径", width = 15)
    @ApiModelProperty(value = "附件路径")
	private java.lang.String filePath;
	/**跟进内容*/
	@Excel(name = "跟进内容", width = 15)
    @ApiModelProperty(value = "跟进内容")
	private java.lang.String remarks;
}
