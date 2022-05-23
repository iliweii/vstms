package org.jeecg.modules.research.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.research.entity.ResearchReq;
import org.jeecg.modules.research.mapper.ResearchReqMapper;
import org.jeecg.modules.research.service.IResearchReqService;
import org.jeecg.modules.system.service.ISysNoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 需求调研
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ResearchReqServiceImpl extends ServiceImpl<ResearchReqMapper, ResearchReq> implements IResearchReqService {

    private final ISysNoService sysNoService;

    @Override
    public void add(ResearchReq researchReq) {
        researchReq.setNo(sysNoService.generateNo("XQ{YM}{5}"));
        researchReq.setStatus("0");
        this.save(researchReq);
    }
}
