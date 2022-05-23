package org.jeecg.modules.training.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.training.entity.TrainingInsurance;
import org.jeecg.modules.training.vo.TrainingInsuranceVO;

/**
 * @Description: 保险办理
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
public interface ITrainingInsuranceService extends IService<TrainingInsurance> {

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类
     */
    IPage<TrainingInsuranceVO> page(Page<TrainingInsuranceVO> page, QueryWrapper<TrainingInsuranceVO> queryWrapper);

    /**
     * 编辑
     *
     * @param trainingInsurance
     */
    void edit(TrainingInsurance trainingInsurance);

}
