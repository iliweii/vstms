package org.jeecg.modules.training.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 课程表
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Data
@TableName("training_class_schedule")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="training_class_schedule对象", description="课程表")
public class TrainingClassSchedule  extends JeecgEntity {

	/**培训班编号*/
	@Excel(name = "培训班编号", width = 15)
    @ApiModelProperty(value = "培训班编号")
	private java.lang.String classNo;
	/**星期几*/
	@Excel(name = "星期几", width = 15)
    @ApiModelProperty(value = "星期几")
	private java.lang.String day;
	/**节次*/
	@Excel(name = "节次", width = 15)
    @ApiModelProperty(value = "节次")
	private java.lang.Integer several;
	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
    @ApiModelProperty(value = "课程名称")
	private java.lang.String courseName;
	/**教师*/
	@Excel(name = "教师", width = 15)
    @ApiModelProperty(value = "教师")
    @Dict(dicCode = "username", dictTable = "sys_user", dicText = "realname")
	private java.lang.String teacher;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String remark;

}
