package org.jeecg.modules.training.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.service.ISysNoService;
import org.jeecg.modules.training.entity.TrainingClass;
import org.jeecg.modules.training.mapper.TrainingClassMapper;
import org.jeecg.modules.training.service.ITrainingClassService;
import org.jeecg.modules.training.vo.TrainingClassVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 培训班
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TrainingClassServiceImpl extends ServiceImpl<TrainingClassMapper, TrainingClass> implements ITrainingClassService {

    private final ISysNoService sysNoService;

    @Override
    public IPage<TrainingClassVO> page(Page<TrainingClassVO> page, QueryWrapper<TrainingClassVO> queryWrapper) {
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void add(TrainingClass trainingClass) {
        trainingClass.setNo(sysNoService.generateNo("PXB-{Y}{3}"));
        trainingClass.setDelFlag("0").setStatus("0");

        this.save(trainingClass);
    }

    @Override
    public TrainingClass getByNo(String no) {
        return this.lambdaQuery().eq(TrainingClass::getNo, no).one();
    }
}
