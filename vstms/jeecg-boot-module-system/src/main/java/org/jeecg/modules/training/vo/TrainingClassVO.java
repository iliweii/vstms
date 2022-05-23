package org.jeecg.modules.training.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.training.entity.TrainingClass;

@Data
public class TrainingClassVO extends TrainingClass {

    @ApiModelProperty(value = "教师数量")
    private java.lang.Integer teacherRealNum;
    @ApiModelProperty(value = "学生数量")
    private java.lang.Integer studentRealNum;

}
