package org.jeecg.modules.training.entity;

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
 * @Description: 考勤管理
 * @Author: jeecg-boot
 * @Date:   2022-05-25
 * @Version: V1.0
 */
@Data
@TableName("training_attendance")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="training_attendance对象", description="考勤管理")
public class TrainingAttendance extends JeecgEntity {

	/**培训班编号*/
	@Excel(name = "培训班编号", width = 15)
    @ApiModelProperty(value = "培训班编号")
	private java.lang.String classNo;
	/**学员用户名*/
	@Excel(name = "学员用户名", width = 15)
    @ApiModelProperty(value = "学员用户名")
	@Dict(dicCode = "username", dictTable = "sys_user", dicText = "realname")
	private java.lang.String username;
	/**考勤日期*/
	@Excel(name = "考勤日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "考勤日期")
	private java.util.Date atdDate;
	/**考勤状态 0出勤 1迟到 2旷课 3请假*/
	@Excel(name = "考勤状态 0出勤 1迟到 2旷课 3请假", width = 15)
    @ApiModelProperty(value = "考勤状态 0出勤 1迟到 2旷课 3请假")
	@Dict(dicCode = "training_attendance_status")
	private java.lang.String atdStatus;

}
