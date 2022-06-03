package org.jeecg.modules.tracking.entity;

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
 * @Description: 视导记录
 * @Author: jeecg-boot
 * @Date:   2022-06-03
 * @Version: V1.0
 */
@Data
@TableName("tracking_supervision")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tracking_supervision对象", description="视导记录")
public class TrackingSupervision extends JeecgEntity {

	/**培训班编号*/
	@Excel(name = "培训班编号", width = 15)
    @ApiModelProperty(value = "培训班编号")
	private java.lang.String classNo;
	/**视导日期*/
	@Excel(name = "视导日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "视导日期")
	private java.util.Date svTime;
	/**基本情况*/
	@Excel(name = "基本情况", width = 15)
    @ApiModelProperty(value = "基本情况")
	private java.lang.String situation;
	/**值得肯定的成绩*/
	@Excel(name = "值得肯定的成绩", width = 15)
    @ApiModelProperty(value = "值得肯定的成绩")
	private java.lang.String excellent;
	/**存在的问题和不足*/
	@Excel(name = "存在的问题和不足", width = 15)
    @ApiModelProperty(value = "存在的问题和不足")
	private java.lang.String problems;
	/**改进的意见或建议*/
	@Excel(name = "改进的意见或建议", width = 15)
    @ApiModelProperty(value = "改进的意见或建议")
	private java.lang.String suggestions;

}
