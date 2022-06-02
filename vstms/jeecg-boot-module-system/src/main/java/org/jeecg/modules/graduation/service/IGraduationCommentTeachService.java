package org.jeecg.modules.graduation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.graduation.entity.GraduationCommentTeach;

/**
 * @Description: 评教
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
public interface IGraduationCommentTeachService extends IService<GraduationCommentTeach> {


    /**
     * 某学生的评教分页列表
     *
     * @param page
     * @param queryWrapper
     * @param student
     * @return
     */
    IPage<GraduationCommentTeach> studentCommentPage(Page<GraduationCommentTeach> page, QueryWrapper<GraduationCommentTeach> queryWrapper, String student);

}
