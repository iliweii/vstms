package org.jeecg.modules.training.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TrainingLinkModel {

    @ApiModelProperty(value = "位置ID数组")
    private List<String> usernameList;

    @ApiModelProperty(value = "培训班编码")
    private String classNo;

}
