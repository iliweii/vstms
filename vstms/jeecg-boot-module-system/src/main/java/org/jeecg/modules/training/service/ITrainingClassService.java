package org.jeecg.modules.training.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.training.entity.TrainingClass;
import org.jeecg.modules.training.vo.TrainingClassVO;

/**
 * @Description: 培训班
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
public interface ITrainingClassService extends IService<TrainingClass> {

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类
     */
    IPage<TrainingClassVO> page(Page<TrainingClassVO> page, QueryWrapper<TrainingClassVO> queryWrapper);

    /**
     * 新增
     *
     * @param trainingClass
     */
    void add(TrainingClass trainingClass);

    /**
     * 根据编号获取培训班
     *
     * @return
     */
    TrainingClass getByNo(String no);

}
