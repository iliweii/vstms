package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 流水号记录表
 * 生成后的流水号记录在此表中，用于继续生成
 */
@Data
@TableName("sys_no")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_no对象", description = "流水号表")
public class SysNo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * 前缀
     */
    @ApiModelProperty(value = "前缀")
    private String prefix;

    /**
     * 流水号
     */
    @ApiModelProperty(value = "流水号")
    private int no;

    /**
     * 完整流水号
     */
    @TableField(exist = false)
    private String fullNo;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String status;

    private String remark;
}
