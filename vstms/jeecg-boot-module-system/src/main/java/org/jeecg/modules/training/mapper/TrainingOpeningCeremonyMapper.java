package org.jeecg.modules.training.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.training.entity.TrainingOpeningCeremony;

/**
 * @Description: 开班典礼
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
public interface TrainingOpeningCeremonyMapper extends BaseMapper<TrainingOpeningCeremony> {

    /**
     * 分页查询
     *
     * @param page         分页参数
     * @param queryWrapper 查询构造器
     * @return 分页结果
     */
    IPage<TrainingOpeningCeremony> selectPage(Page<TrainingOpeningCeremony> page, @Param(Constants.WRAPPER) QueryWrapper<TrainingOpeningCeremony> queryWrapper);

}
