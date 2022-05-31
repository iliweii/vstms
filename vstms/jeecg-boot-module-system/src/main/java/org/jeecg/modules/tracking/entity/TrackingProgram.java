package org.jeecg.modules.tracking.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 训后跟踪
 * @Author: jeecg-boot
 * @Date:   2022-05-31
 * @Version: V1.0
 */
@Data
@TableName("tracking_program")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tracking_program对象", description="训后跟踪")
public class TrackingProgram extends JeecgEntity {

	/**跟踪编码*/
	@Excel(name = "跟踪编码", width = 15)
    @ApiModelProperty(value = "跟踪编码")
	private java.lang.String no;
	/**跟踪名称*/
	@Excel(name = "跟踪名称", width = 15)
    @ApiModelProperty(value = "跟踪名称")
	private java.lang.String title;
	/**跟踪描述*/
	@Excel(name = "跟踪描述", width = 15)
    @ApiModelProperty(value = "跟踪描述")
	private java.lang.Object description;
	/**跟踪状态 0草稿 1制定方案中 2跟踪过程中 3已完成报告*/
	@Excel(name = "跟踪状态 0草稿 1制定方案中 2跟踪过程中 3已完成报告", width = 15)
    @ApiModelProperty(value = "跟踪状态 0草稿 1制定方案中 2跟踪过程中 3已完成报告")
	@Dict(dicCode = "tracking_status")
	private java.lang.String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String remark;

}
