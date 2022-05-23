package org.jeecg.modules.training.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.training.entity.TrainingInsurance;

@Data
public class TrainingInsuranceVO extends TrainingInsurance {

    @ApiModelProperty(value = "头像")
    private String avatar;

}
