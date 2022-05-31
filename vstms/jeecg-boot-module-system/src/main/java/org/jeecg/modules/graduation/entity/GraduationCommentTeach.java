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
 * @Description: 评教
 * @Author: jeecg-boot
 * @Date:   2022-05-31
 * @Version: V1.0
 */
@Data
@TableName("graduation_comment_teach")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="graduation_comment_teach对象", description="评教")
public class GraduationCommentTeach extends JeecgEntity {

	/**培训班编号*/
	@Excel(name = "培训班编号", width = 15)
    @ApiModelProperty(value = "培训班编号")
	private java.lang.String classNo;
	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
    @ApiModelProperty(value = "课程名称")
	private java.lang.String courseName;
	/**教师信息*/
	@Excel(name = "教师信息", width = 15)
    @ApiModelProperty(value = "教师信息")
	@Dict(dicCode = "username", dictTable = "sys_user", dicText = "realname")
	private java.lang.String teacher;
	/**分数*/
	@Excel(name = "分数", width = 15)
    @ApiModelProperty(value = "分数")
	private java.lang.Integer score;
	/**评语*/
	@Excel(name = "评语", width = 15)
    @ApiModelProperty(value = "评语")
	private java.lang.String comments;

}
