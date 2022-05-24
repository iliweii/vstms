package org.jeecg.modules.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.service.ISysNoService;
import org.jeecg.modules.training.entity.TrainingLog;
import org.jeecg.modules.training.mapper.TrainingLogMapper;
import org.jeecg.modules.training.service.ITrainingLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 培训日志
 * @Author: jeecg-boot
 * @Date: 2022-05-25
 * @Version: V1.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TrainingLogServiceImpl extends ServiceImpl<TrainingLogMapper, TrainingLog> implements ITrainingLogService {

    private final ISysNoService sysNoService;

    @Override
    public void add(TrainingLog trainingLog) {
        trainingLog.setDelFlag("0");
        String prefix = "";
        switch (trainingLog.getType()) {
            case "1":
                prefix = "H";
                break;
            case "2":
                prefix = "T";
                break;
            case "3":
                prefix = "STU";
                break;
            case "4":
                prefix = "A";
                break;
            default:
                prefix = "OTHER";
        }
        trainingLog.setNo(sysNoService.generateNo("LOG-" + prefix + "{YM}{3}"));

        this.save(trainingLog);
    }
}
