package org.jeecg.modules.tracking.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 经费支出
 * @Author: jeecg-boot
 * @Date:   2022-06-03
 * @Version: V1.0
 */
@Data
@TableName("tracking_expenditure")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="tracking_expenditure对象", description="经费支出")
public class TrackingExpenditure extends JeecgEntity {

	/**培训班编号*/
	@Excel(name = "培训班编号", width = 15)
    @ApiModelProperty(value = "培训班编号")
	private java.lang.String classNo;
	/**支出类型*/
	@Excel(name = "支出类型", width = 15)
    @ApiModelProperty(value = "支出类型")
	@Dict(dicCode = "tracking_expenditure_type")
	private java.lang.String type;
	/** 支出日期 */
	@ApiModelProperty(value = "支出日期")
	@Excel(name = "支出日期", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private java.util.Date costDate;
	/**支出金额(元)*/
	@Excel(name = "支出金额(元)", width = 15)
    @ApiModelProperty(value = "支出金额(元)")
	private java.math.BigDecimal expend;
	/**支出项目*/
	@Excel(name = "支出项目", width = 15)
    @ApiModelProperty(value = "支出项目")
	private java.lang.String project;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String remark;

}
