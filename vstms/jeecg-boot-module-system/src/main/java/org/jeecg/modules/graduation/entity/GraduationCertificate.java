package org.jeecg.modules.graduation.entity;

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
 * @Description: 证书编号表
 * @Author: jeecg-boot
 * @Date:   2022-06-03
 * @Version: V1.0
 */
@Data
@TableName("graduation_certificate")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="graduation_certificate对象", description="证书编号表")
public class GraduationCertificate extends JeecgEntity {

	/**证书编号*/
	@Excel(name = "证书编号", width = 15)
    @ApiModelProperty(value = "证书编号")
	private java.lang.String no;
	/**培训班编号*/
	@Excel(name = "培训班编号", width = 15)
    @ApiModelProperty(value = "培训班编号")
	private java.lang.String classNo;
	/**职业技能名称*/
	@Excel(name = "职业技能名称", width = 15)
    @ApiModelProperty(value = "职业技能名称")
	private java.lang.String skillName;
	/**学员用户名*/
	@Excel(name = "学员用户名", width = 15)
    @ApiModelProperty(value = "学员用户名")
	@Dict(dicCode = "username", dictTable = "sys_user", dicText = "realname")
	private java.lang.String student;
	/**学员姓名*/
	@Excel(name = "学员姓名", width = 15)
    @ApiModelProperty(value = "学员姓名")
	private java.lang.String studentName;
	/**学员身份证号*/
	@Excel(name = "学员身份证号", width = 15)
    @ApiModelProperty(value = "学员身份证号")
	private java.lang.String studentId;
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
	@Dict(dicCode = "graduation_certificate_status")
	private java.lang.String status;

}
