package org.jeecg.modules.training.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: 培训日志
 * @Author: jeecg-boot
 * @Date:   2022-05-25
 * @Version: V1.0
 */
@Data
@TableName("training_log")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="training_log对象", description="培训日志")
public class TrainingLog extends JeecgEntity {

	/**日志编号*/
	@Excel(name = "日志编号", width = 15)
    @ApiModelProperty(value = "日志编号")
	private java.lang.String no;
	/**日志内容*/
	@Excel(name = "日志内容", width = 15)
    @ApiModelProperty(value = "日志内容")
	private java.lang.String content;
	/**日志类型 班主任 任课教师 学员 督导*/
	@Excel(name = "日志类型 班主任 任课教师 学员 督导", width = 15)
    @ApiModelProperty(value = "日志类型 班主任 任课教师 学员 督导")
	@Dict(dicCode = "training_log_type")
	private java.lang.String type;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
    @ApiModelProperty(value = "删除状态")
	@TableLogic
	private java.lang.String delFlag;

}
