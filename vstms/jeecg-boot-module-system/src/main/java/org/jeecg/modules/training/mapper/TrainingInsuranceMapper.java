package org.jeecg.modules.training.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.training.entity.TrainingInsurance;
import org.jeecg.modules.training.vo.TrainingInsuranceVO;

/**
 * @Description: 保险办理
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
public interface TrainingInsuranceMapper extends BaseMapper<TrainingInsurance> {

    /**
     * 分页查询
     *
     * @param page         分页参数
     * @param queryWrapper 查询构造器
     * @return 分页结果
     */
    IPage<TrainingInsuranceVO> selectPage(Page<TrainingInsuranceVO> page, @Param(Constants.WRAPPER) QueryWrapper<TrainingInsuranceVO> queryWrapper);

}
