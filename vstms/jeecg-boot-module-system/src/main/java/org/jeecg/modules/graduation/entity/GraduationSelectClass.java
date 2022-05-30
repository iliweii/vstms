package org.jeecg.modules.graduation.entity;

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
 * @Description: 评选优秀学员
 * @Author: jeecg-boot
 * @Date:   2022-05-31
 * @Version: V1.0
 */
@Data
@TableName("graduation_select_class")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="graduation_select_class对象", description="评选优秀学员")
public class GraduationSelectClass extends JeecgEntity {

	/**培训班编号*/
	@Excel(name = "培训班编号", width = 15)
    @ApiModelProperty(value = "培训班编号")
	@Dict(dicCode = "no", dictTable = "training_class", dicText = "name")
	private java.lang.String classNo;
	/**评选状态 录入状态 待确认 制定方案中 评选中 完成*/
	@Excel(name = "评选状态 录入状态 待确认 制定方案中 评选中 完成", width = 15)
    @ApiModelProperty(value = "评选状态 录入状态 待确认 制定方案中 评选中 完成")
	@Dict(dicCode = "graduation_class_select_status")
	private java.lang.String status;

}
