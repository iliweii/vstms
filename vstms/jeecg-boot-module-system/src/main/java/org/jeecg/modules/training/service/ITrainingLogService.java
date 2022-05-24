package org.jeecg.modules.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.training.entity.TrainingLog;

/**
 * @Description: 培训日志
 * @Author: jeecg-boot
 * @Date: 2022-05-25
 * @Version: V1.0
 */
public interface ITrainingLogService extends IService<TrainingLog> {

    /**
     * 新增
     *
     * @param trainingLog
     */
    void add(TrainingLog trainingLog);

}
