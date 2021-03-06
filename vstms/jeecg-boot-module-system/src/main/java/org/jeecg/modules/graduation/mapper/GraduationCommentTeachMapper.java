package org.jeecg.modules.graduation.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.graduation.entity.GraduationCommentTeach;

/**
 * @Description: 评教
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
public interface GraduationCommentTeachMapper extends BaseMapper<GraduationCommentTeach> {

    /**
     * 分页查询
     *
     * @param page         分页参数
     * @param queryWrapper 查询构造器
     * @return 分页结果
     */
    IPage<GraduationCommentTeach> selectPage(Page<GraduationCommentTeach> page, @Param(Constants.WRAPPER) QueryWrapper<GraduationCommentTeach> queryWrapper);

    /**
     * 分页查询某学生的评教列表
     *
     * @param page         分页参数
     * @param queryWrapper 查询构造器
     * @param student      学生用户名
     * @return 分页结果
     */
    IPage<GraduationCommentTeach> selectCTPage(Page<GraduationCommentTeach> page, @Param(Constants.WRAPPER) QueryWrapper<GraduationCommentTeach> queryWrapper, String student);

}
