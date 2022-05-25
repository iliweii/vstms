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
 * @Description: 培训总结表
 * @Author: jeecg-boot
 * @Date:   2022-05-25
 * @Version: V1.0
 */
@Data
@TableName("training_summary")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="training_summary对象", description="培训总结表")
public class TrainingSummary extends JeecgEntity {

	/**总结编号*/
	@Excel(name = "总结编号", width = 15)
    @ApiModelProperty(value = "总结编号")
	private java.lang.String no;
	/**总结内容*/
	@Excel(name = "总结内容", width = 15)
    @ApiModelProperty(value = "总结内容")
	private java.lang.String content;
	/**总结类型 学员感言 学员培训总结 教师自评报告 培训典型案例 督导总结 培训班总结*/
	@Excel(name = "总结类型 学员感言 学员培训总结 教师自评报告 培训典型案例 督导总结 培训班总结", width = 15)
    @ApiModelProperty(value = "总结类型 学员感言 学员培训总结 教师自评报告 培训典型案例 督导总结 培训班总结")
    @Dict(dicCode = "training_summary_type")
	private java.lang.String type;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
    @ApiModelProperty(value = "删除状态")
    @TableLogic
	private java.lang.String delFlag;

}
