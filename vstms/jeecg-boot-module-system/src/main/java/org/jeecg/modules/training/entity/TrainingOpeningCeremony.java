package org.jeecg.modules.training.entity;

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
 * @Description: 开班典礼
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Data
@TableName("training_opening_ceremony")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="training_opening_ceremony对象", description="开班典礼")
public class TrainingOpeningCeremony extends JeecgEntity {

	/**培训班编号*/
	@Excel(name = "培训班编号", width = 15)
    @ApiModelProperty(value = "培训班编号")
	private java.lang.String classNo;
	/**举行日期*/
	@Excel(name = "举行日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "举行日期")
	private java.util.Date holdDate;
	/**实际举行日期*/
	@Excel(name = "实际举行日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "实际举行日期")
	private java.util.Date realDate;
	/**参与人数*/
	@Excel(name = "参与人数", width = 15)
    @ApiModelProperty(value = "参与人数")
	private java.lang.Integer referNum;
	/**实际参与人数*/
	@Excel(name = "实际参与人数", width = 15)
    @ApiModelProperty(value = "实际参与人数")
	private java.lang.Integer realNum;
	/**典礼状态 0未确认 1制定方案中 2准备开始中 3已完成*/
	@Excel(name = "典礼状态 0未确认 1制定方案中 2准备开始中 3已完成", width = 15)
    @ApiModelProperty(value = "典礼状态 0未确认 1制定方案中 2准备开始中 3已完成")
	@Dict(dicCode = "training_opening_ceremony_status")
	private java.lang.String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String remark;

}