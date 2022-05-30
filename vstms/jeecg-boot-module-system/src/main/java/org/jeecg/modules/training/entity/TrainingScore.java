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
 * @Description: 成绩管理
 * @Author: jeecg-boot
 * @Date:   2022-05-30
 * @Version: V1.0
 */
@Data
@TableName("training_score")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="training_score对象", description="成绩管理")
public class TrainingScore extends JeecgEntity {

	/**培训班编号*/
	@Excel(name = "培训班编号", width = 15)
    @ApiModelProperty(value = "培训班编号")
	private java.lang.String classNo;
	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
    @ApiModelProperty(value = "课程名称")
	private java.lang.String courseName;
	/**学员用户名*/
	@Excel(name = "学员用户名", width = 15)
    @ApiModelProperty(value = "学员用户名")
	@Dict(dicCode = "username", dictTable = "sys_user", dicText = "realname")
	private java.lang.String username;
	/**成绩*/
	@Excel(name = "成绩", width = 15)
    @ApiModelProperty(value = "成绩")
	private java.math.BigDecimal score;

}
