package org.jeecg.modules.tracking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.service.ISysNoService;
import org.jeecg.modules.tracking.entity.TrackingProgram;
import org.jeecg.modules.tracking.mapper.TrackingProgramMapper;
import org.jeecg.modules.tracking.service.ITrackingProgramService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 训后跟踪
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TrackingProgramServiceImpl extends ServiceImpl<TrackingProgramMapper, TrackingProgram> implements ITrackingProgramService {

    private final ISysNoService sysNoService;

    @Override
    public void add(TrackingProgram trackingProgram) {

        trackingProgram.setNo(sysNoService.generateNo("TRACK{YM}{5}"));
        trackingProgram.setStatus("0");
        this.save(trackingProgram);

    }
}
