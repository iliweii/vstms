package org.jeecg.modules.training.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.training.entity.TrainingClassStudent;

@Data
public class TrainingClassStudentVO extends TrainingClassStudent {

    @ApiModelProperty(value = "头像")
    private java.lang.String avatar;

}
