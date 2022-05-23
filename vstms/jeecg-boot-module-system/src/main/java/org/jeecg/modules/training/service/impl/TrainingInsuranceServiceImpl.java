package org.jeecg.modules.training.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jeecg.modules.training.entity.TrainingInsurance;
import org.jeecg.modules.training.mapper.TrainingInsuranceMapper;
import org.jeecg.modules.training.service.ITrainingInsuranceService;
import org.jeecg.modules.training.vo.TrainingInsuranceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 保险办理
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TrainingInsuranceServiceImpl extends ServiceImpl<TrainingInsuranceMapper, TrainingInsurance> implements ITrainingInsuranceService {

    @Override
    public IPage<TrainingInsuranceVO> page(Page<TrainingInsuranceVO> page, QueryWrapper<TrainingInsuranceVO> queryWrapper) {
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void edit(TrainingInsurance trainingInsurance) {
        // 根据培训班编码 和 用户确定一条数据
        TrainingInsurance insurancceQuery = this.lambdaQuery().eq(TrainingInsurance::getClassNo, trainingInsurance.getClassNo())
                .eq(TrainingInsurance::getUsername, trainingInsurance.getUsername()).one();
        if (ObjectUtils.isNotEmpty(insurancceQuery)) {
            // 编辑
            trainingInsurance.setId(insurancceQuery.getId());
            this.updateById(trainingInsurance);
        } else {
            // 新增
            trainingInsurance.setId(null);
            this.save(trainingInsurance);
        }
    }
}
