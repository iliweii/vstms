package org.jeecg.modules.training.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.training.entity.TrainingReceive;
import org.jeecg.modules.training.mapper.TrainingReceiveMapper;
import org.jeecg.modules.training.service.ITrainingReceiveService;
import org.jeecg.modules.training.vo.TrainingReceiveVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 资料领取
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TrainingReceiveServiceImpl extends ServiceImpl<TrainingReceiveMapper, TrainingReceive> implements ITrainingReceiveService {

    @Override
    public IPage<TrainingReceiveVO> page(Page<TrainingReceiveVO> page, QueryWrapper<TrainingReceiveVO> queryWrapper) {
        return this.baseMapper.selectPage(page, queryWrapper);
    }
}
