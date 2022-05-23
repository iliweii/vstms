package org.jeecg.modules.training.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.training.entity.TrainingReceive;
import org.jeecg.modules.training.vo.TrainingReceiveVO;

/**
 * @Description: 资料领取
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
public interface ITrainingReceiveService extends IService<TrainingReceive> {

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类
     */
    IPage<TrainingReceiveVO> page(Page<TrainingReceiveVO> page, QueryWrapper<TrainingReceiveVO> queryWrapper);

}
