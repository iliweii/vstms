package org.jeecg.modules.research.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.research.entity.ResearchReq;

/**
 * @Description: 需求调研
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
public interface IResearchReqService extends IService<ResearchReq> {

    /**
     * 新增
     *
     * @param researchReq
     */
    void add(ResearchReq researchReq);

}
