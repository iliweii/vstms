package org.jeecg.modules.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.training.entity.TrainingClassStudent;
import org.jeecg.modules.training.vo.TrainingLinkModel;

/**
 * @Description: 培训班学生关系
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
public interface ITrainingClassStudentService extends IService<TrainingClassStudent> {

    /**
     * 编辑
     *
     * @param trainingLinkModel
     */
    void edit(TrainingLinkModel trainingLinkModel);

}
