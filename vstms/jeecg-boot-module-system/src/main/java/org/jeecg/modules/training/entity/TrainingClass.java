package org.jeecg.modules.training.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: 培训班
 * @Author: jeecg-boot
 * @Date:   2022-05-23
 * @Version: V1.0
 */
@Data
@TableName("training_class")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="training_class对象", description="培训班")
public class TrainingClass extends JeecgEntity {

	/**编号*/
	@Excel(name = "编号", width = 15)
    @ApiModelProperty(value = "编号")
	private java.lang.String no;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
	private java.lang.String description;
	/**课时*/
	@Excel(name = "课时", width = 15)
    @ApiModelProperty(value = "课时")
	private java.lang.Integer period;
	/**价格(元)*/
	@Excel(name = "价格(元)", width = 15)
    @ApiModelProperty(value = "价格(元)")
	private java.math.BigDecimal price;
	/**参考教师数量*/
	@Excel(name = "参考教师数量", width = 15)
    @ApiModelProperty(value = "参考教师数量")
	private java.lang.Integer teacherNum;
	/**参考学生数量*/
	@Excel(name = "参考学生数量", width = 15)
    @ApiModelProperty(value = "参考学生数量")
	private java.lang.Integer studentNum;
	/**职业技能名称*/
	@Excel(name = "职业技能名称", width = 15)
	@ApiModelProperty(value = "职业技能名称")
	private java.lang.String skillName;
	/**状态 0草稿 1录入信息中 2通过 3禁用*/
	@Excel(name = "状态 0草稿 1录入信息中 2通过 3禁用", width = 15)
    @ApiModelProperty(value = "状态 0草稿 1录入信息中 2通过 3禁用")
	@Dict(dicCode = "training_class_status")
	private java.lang.String status;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
    @ApiModelProperty(value = "删除状态")
	@TableLogic
	private java.lang.String delFlag;

}
