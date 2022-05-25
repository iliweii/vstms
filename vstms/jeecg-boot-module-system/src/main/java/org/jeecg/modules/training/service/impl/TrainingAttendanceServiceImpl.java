package org.jeecg.modules.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.training.entity.TrainingAttendance;
import org.jeecg.modules.training.mapper.TrainingAttendanceMapper;
import org.jeecg.modules.training.service.ITrainingAttendanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 考勤管理
 * @Author: jeecg-boot
 * @Date: 2022-05-25
 * @Version: V1.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TrainingAttendanceServiceImpl extends ServiceImpl<TrainingAttendanceMapper, TrainingAttendance> implements ITrainingAttendanceService {

    @Override
    public ModelAndView exportTemplate(String classNo) {
        return null;
    }
}
