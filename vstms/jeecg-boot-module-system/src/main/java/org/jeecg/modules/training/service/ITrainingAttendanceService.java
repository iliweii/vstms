package org.jeecg.modules.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.training.entity.TrainingAttendance;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 考勤管理
 * @Author: jeecg-boot
 * @Date: 2022-05-25
 * @Version: V1.0
 */
public interface ITrainingAttendanceService extends IService<TrainingAttendance> {

    /**
     * 下载导入模板
     *
     * @return
     */
    ModelAndView exportTemplate(String classNo);

}
