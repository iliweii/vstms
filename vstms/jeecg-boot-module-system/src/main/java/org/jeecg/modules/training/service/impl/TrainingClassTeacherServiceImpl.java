package org.jeecg.modules.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.training.entity.TrainingClassTeacher;
import org.jeecg.modules.training.mapper.TrainingClassTeacherMapper;
import org.jeecg.modules.training.service.ITrainingClassTeacherService;
import org.jeecg.modules.training.vo.TrainingLinkModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description: 培训班教师关系
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TrainingClassTeacherServiceImpl extends ServiceImpl<TrainingClassTeacherMapper, TrainingClassTeacher> implements ITrainingClassTeacherService {

    @Override
    public void edit(TrainingLinkModel trainingLinkModel) {
        // 批量新增关系
        List<TrainingClassTeacher> addList = new LinkedList<>();
        trainingLinkModel.getUsernameList().forEach(username -> {
            TrainingClassTeacher rel = new TrainingClassTeacher();
            rel.setClassNo(trainingLinkModel.getClassNo())
                    .setUsername(username)
                    .setIsHead("0");
            addList.add(rel);
        });
        this.saveBatch(addList);
    }

}
