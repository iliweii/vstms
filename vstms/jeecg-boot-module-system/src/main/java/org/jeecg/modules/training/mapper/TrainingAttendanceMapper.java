package org.jeecg.modules.training.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.training.entity.TrainingAttendance;
import org.jeecg.modules.training.excel.AttendanceSheetExport;

import java.util.List;

/**
 * @Description: 考勤管理
 * @Author: jeecg-boot
 * @Date: 2022-05-25
 * @Version: V1.0
 */
public interface TrainingAttendanceMapper extends BaseMapper<TrainingAttendance> {


    /**
     * 某培训班学生的考勤情况导出列表
     *
     * @param classNo
     * @return
     */
    List<AttendanceSheetExport> getAttendanceSheet(String classNo, String date);

}
