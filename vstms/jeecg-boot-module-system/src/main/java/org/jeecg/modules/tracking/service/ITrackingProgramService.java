package org.jeecg.modules.tracking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.tracking.entity.TrackingProgram;

/**
 * @Description: 训后跟踪
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
public interface ITrackingProgramService extends IService<TrackingProgram> {

    /**
     * 新增
     *
     * @param trackingProgram
     */
    void add(TrackingProgram trackingProgram);

}
