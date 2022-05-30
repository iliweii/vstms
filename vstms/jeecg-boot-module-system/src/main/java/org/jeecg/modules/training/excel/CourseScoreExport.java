package org.jeecg.modules.training.excel;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

/**
 * 导出的空白成绩导入表，
 * 用于填写并上传成绩情况
 */
@Data
public class CourseScoreExport {

    @Excel(name = "培训班编号(必填)", width = 30)
    private String classNo;
    @Excel(name = "课程名称(必填)", width = 30)
    private String courseName;
    @Excel(name = "姓名(必填)", width = 30, dicCode = "username", dictTable = "sys_user", dicText = "realname")
    private String username;
    @Excel(name = "成绩(必填)", width = 30)
    private BigDecimal score;

}
