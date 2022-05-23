package org.jeecg.modules.research.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 需求调研
 * @Author: jeecg-boot
 * @Date:   2022-05-23
 * @Version: V1.0
 */
@Data
@TableName("research_req")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="research_req对象", description="需求调研")
public class ResearchReq extends JeecgEntity implements Serializable {

	/**需求编码*/
	@Excel(name = "需求编码", width = 15)
    @ApiModelProperty(value = "需求编码")
	private String no;
	/**需求名称*/
	@Excel(name = "需求名称", width = 15)
    @ApiModelProperty(value = "需求名称")
	private String title;
	/**需求名称*/
	// @Excel(name = "需求描述", width = 15)
    @ApiModelProperty(value = "需求描述")
	private String description;
	/**需求状态 0草稿 1制定方案中 2调研过程中 3已完成报告*/
	@Excel(name = "需求状态", width = 15, dicCode = "research_req_status")
    @ApiModelProperty(value = "需求状态 0草稿 1制定方案中 2调研过程中 3已完成报告")
	@Dict(dicCode = "research_req_status")
	private String status;
	/**备注*/
	// @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;

}
