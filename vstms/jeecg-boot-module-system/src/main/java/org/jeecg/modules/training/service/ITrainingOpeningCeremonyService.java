package org.jeecg.modules.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.training.entity.TrainingOpeningCeremony;

/**
 * @Description: 开班典礼
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
public interface ITrainingOpeningCeremonyService extends IService<TrainingOpeningCeremony> {

    /**
     * 编辑
     *
     * @param trainingOpeningCeremony
     */
    void edit(TrainingOpeningCeremony trainingOpeningCeremony);

}
