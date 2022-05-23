package org.jeecg.modules.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.training.entity.TrainingClassStudent;
import org.jeecg.modules.training.mapper.TrainingClassStudentMapper;
import org.jeecg.modules.training.service.ITrainingClassStudentService;
import org.jeecg.modules.training.vo.TrainingLinkModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description: 培训班学生关系
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TrainingClassStudentServiceImpl extends ServiceImpl<TrainingClassStudentMapper, TrainingClassStudent> implements ITrainingClassStudentService {

    @Override
    public void edit(TrainingLinkModel trainingLinkModel) {
        // 批量新增关系
        List<TrainingClassStudent> addList = new LinkedList<>();
        trainingLinkModel.getUsernameList().forEach(username -> {
            TrainingClassStudent rel = new TrainingClassStudent();
            rel.setClassNo(trainingLinkModel.getClassNo())
                    .setUsername(username)
                    .setStatus("0");
            addList.add(rel);
        });
        this.saveBatch(addList);
    }
}
