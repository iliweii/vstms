package org.jeecg.modules.graduation.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.graduation.entity.GraduationSelectClass;

/**
 * @Description: 评选优秀学员
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
public interface GraduationSelectClassMapper extends BaseMapper<GraduationSelectClass> {

    /**
     * 分页查询
     *
     * @param page         分页参数
     * @param queryWrapper 查询构造器
     * @return 分页结果
     */
    IPage<GraduationSelectClass> selectPage(Page<GraduationSelectClass> page, @Param(Constants.WRAPPER) QueryWrapper<GraduationSelectClass> queryWrapper);

}
