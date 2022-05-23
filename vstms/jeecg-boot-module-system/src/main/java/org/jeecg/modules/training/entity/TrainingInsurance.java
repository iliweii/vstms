package org.jeecg.modules.training.entity;

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
 * @Description: 保险办理
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Data
@TableName("training_insurance")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="training_insurance对象", description="保险办理")
public class TrainingInsurance extends JeecgEntity {

	/**培训班编号*/
	@Excel(name = "培训班编号", width = 15)
    @ApiModelProperty(value = "培训班编号")
	private java.lang.String classNo;
	/**用户名*/
	@Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "用户名")
    @Dict(dicCode = "username", dictTable = "sys_user", dicText = "realname")
	private java.lang.String username;
	/**办理状态 0未办理 1已办理*/
	@Excel(name = "办理状态 0未办理 1已办理", width = 15)
    @ApiModelProperty(value = "办理状态 0未办理 1已办理")
    @Dict(dicCode = "training_insurance_status")
	private java.lang.String status;
}
