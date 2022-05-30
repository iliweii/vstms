package org.jeecg.modules.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.training.entity.TrainingClassSchedule;
import org.jeecg.modules.training.mapper.TrainingClassScheduleMapper;
import org.jeecg.modules.training.service.ITrainingClassScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 课程表
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TrainingClassScheduleServiceImpl extends ServiceImpl<TrainingClassScheduleMapper, TrainingClassSchedule> implements ITrainingClassScheduleService {

    @Override
    public List<String> getCourseList(String classNo) {
        List<TrainingClassSchedule> schedules = this.lambdaQuery()
                .eq(TrainingClassSchedule::getClassNo, classNo)
                .list();
        List<String> courseList = schedules.stream().map(TrainingClassSchedule::getCourseName).collect(Collectors.toList());
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(courseList);
        ArrayList<String> listWithoutDuplicates = new ArrayList<>(hashSet);
        return listWithoutDuplicates;
    }
}
