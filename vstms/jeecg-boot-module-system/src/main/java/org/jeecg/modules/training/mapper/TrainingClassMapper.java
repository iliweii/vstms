package org.jeecg.modules.training.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.training.entity.TrainingClass;
import org.jeecg.modules.training.vo.TrainingClassVO;

/**
 * @Description: 培训班
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
public interface TrainingClassMapper extends BaseMapper<TrainingClass> {

    /**
     * 分页查询
     *
     * @param page         分页参数
     * @param queryWrapper 查询构造器
     * @return 分页结果
     */
    IPage<TrainingClassVO> selectPage(Page<TrainingClassVO> page, @Param(Constants.WRAPPER) QueryWrapper<TrainingClassVO> queryWrapper);


}
