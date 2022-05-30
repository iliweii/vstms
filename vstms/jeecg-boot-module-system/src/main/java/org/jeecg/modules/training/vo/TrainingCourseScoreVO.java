package org.jeecg.modules.training.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

@Data
public class TrainingCourseScoreVO {

    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    @ApiModelProperty(value = "培训班编号")
    private java.lang.String classNo;
    @ApiModelProperty(value = "课程名称")
    private java.lang.String courseName;
    @ApiModelProperty(value = "录入状态 0未录入 1已录入")
    @Dict(dicCode = "training_course_score_status")
    private java.lang.String status;
    @ApiModelProperty(value = "教师 逗号分割")
    @Dict(dicCode = "username", dictTable = "sys_user", dicText = "realname")
    private java.lang.String teacher;
}
