package org.jeecg.modules.training.excel;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class AttendanceSheetImport {

    @Excel(name = "培训班编号(必填)", width = 30)
    private java.lang.String classNo;
    @Excel(name = "姓名(必填)", width = 30, dicCode = "username", dictTable = "sys_user", dicText = "realname")
    private java.lang.String username;
    @Excel(name = "考勤日期(必填)", width = 20, format = "yyyy-MM-dd")
    private java.util.Date atdDate;
    @Excel(name = "考勤状态(必填)", width = 30, dicCode = "training_attendance_status")
    private java.lang.String atdStatus;
    @Excel(name = "错误信息", width = 45)
    private String msg;

}
