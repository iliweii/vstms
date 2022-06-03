package org.jeecg.modules.graduation.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 意见建议
 * @Author: jeecg-boot
 * @Date:   2022-06-03
 * @Version: V1.0
 */
@Data
@TableName("graduation_suggestions")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="graduation_suggestions对象", description="意见建议")
public class GraduationSuggestions extends JeecgEntity {

	/**意见编号*/
	@Excel(name = "意见编号", width = 15)
    @ApiModelProperty(value = "意见编号")
	private java.lang.String no;
	/**意见对象 班级 授课教师 班主任 课程 院校 其他*/
	@Excel(name = "意见对象 班级 授课教师 班主任 课程 院校 其他", width = 15)
    @ApiModelProperty(value = "意见对象 班级 授课教师 班主任 课程 院校 其他")
	@Dict(dicCode = "graduation_suggestions_obj")
	private java.lang.String obj;
	/**意见对象值*/
	@Excel(name = "意见对象值", width = 15)
    @ApiModelProperty(value = "意见对象值")
	private java.lang.String objValue;
	/**意见内容*/
	@Excel(name = "意见内容", width = 15)
    @ApiModelProperty(value = "意见内容")
	private java.lang.String content;

}
