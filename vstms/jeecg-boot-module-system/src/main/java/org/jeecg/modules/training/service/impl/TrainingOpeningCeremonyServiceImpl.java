package org.jeecg.modules.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jeecg.modules.training.entity.TrainingOpeningCeremony;
import org.jeecg.modules.training.mapper.TrainingOpeningCeremonyMapper;
import org.jeecg.modules.training.service.ITrainingOpeningCeremonyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 开班典礼
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TrainingOpeningCeremonyServiceImpl extends ServiceImpl<TrainingOpeningCeremonyMapper, TrainingOpeningCeremony> implements ITrainingOpeningCeremonyService {

    @Override
    public void edit(TrainingOpeningCeremony trainingOpeningCeremony) {
        // 根据培训班编码确定一条数据
        TrainingOpeningCeremony openingCeremonyQuery = this.lambdaQuery().eq(TrainingOpeningCeremony::getClassNo, trainingOpeningCeremony.getClassNo()).one();
        if (ObjectUtils.isNotEmpty(openingCeremonyQuery)) {
            // 编辑
            trainingOpeningCeremony.setId(openingCeremonyQuery.getId());
            this.updateById(trainingOpeningCeremony);
        } else {
            // 新增
            trainingOpeningCeremony.setId(null);
            this.save(trainingOpeningCeremony);
        }
    }
}
