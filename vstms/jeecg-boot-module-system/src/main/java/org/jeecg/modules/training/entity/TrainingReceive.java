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
 * @Description: 资料领取
 * @Author: jeecg-boot
 * @Date:   2022-05-24
 * @Version: V1.0
 */
@Data
@TableName("training_receive")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="training_receive对象", description="资料领取")
public class TrainingReceive extends JeecgEntity {

	/**培训班编号*/
	@Excel(name = "培训班编号", width = 15)
    @ApiModelProperty(value = "培训班编号")
	private java.lang.String classNo;
	/**用户名*/
	@Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "用户名")
	@Dict(dicCode = "username", dictTable = "sys_user", dicText = "realname")
	private java.lang.String username;
	/**领取状态 0未领 1已领*/
	@Excel(name = "领取状态 0未领 1已领", width = 15)
    @ApiModelProperty(value = "领取状态 0未领 1已领")
	@Dict(dicCode = "training_receive_status")
	private java.lang.String status;

}
