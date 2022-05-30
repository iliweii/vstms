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
@TableName("graduation_select_student")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="graduation_select_student对象", description="评选优秀学员")
public class GraduationSelectStudent extends JeecgEntity {

	/**培训班编号*/
	@Excel(name = "培训班编号", width = 15)
    @ApiModelProperty(value = "培训班编号")
	private java.lang.String classNo;
	/**优秀学员用户名*/
	@Excel(name = "优秀学员用户名", width = 15)
    @ApiModelProperty(value = "优秀学员用户名")
	@Dict(dicCode = "username", dictTable = "sys_user", dicText = "realname")
	private java.lang.String student;
	/**评选状态 0评选列表 1优秀列表*/
	@Excel(name = "评选状态 0评选列表 1优秀列表", width = 15)
    @ApiModelProperty(value = "评选状态 0评选列表 1优秀列表")
	private java.lang.String status;

}
