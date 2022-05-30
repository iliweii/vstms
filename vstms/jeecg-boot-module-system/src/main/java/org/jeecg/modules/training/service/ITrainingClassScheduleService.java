package org.jeecg.modules.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.training.entity.TrainingClassSchedule;

import java.util.List;

/**
 * @Description: 课程表
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
public interface ITrainingClassScheduleService extends IService<TrainingClassSchedule> {

    /**
     * 根据培训班号获取课程名称列表
     *
     * @param classNo
     * @return
     */
    List<String> getCourseList(String classNo);

}
