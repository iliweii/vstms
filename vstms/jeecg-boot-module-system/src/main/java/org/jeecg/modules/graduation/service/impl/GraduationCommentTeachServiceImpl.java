package org.jeecg.modules.graduation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.graduation.entity.GraduationCommentTeach;
import org.jeecg.modules.graduation.mapper.GraduationCommentTeachMapper;
import org.jeecg.modules.graduation.service.IGraduationCommentTeachService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 评教
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GraduationCommentTeachServiceImpl extends ServiceImpl<GraduationCommentTeachMapper, GraduationCommentTeach> implements IGraduationCommentTeachService {

    @Override
    public IPage<GraduationCommentTeach> studentCommentPage(Page<GraduationCommentTeach> page, QueryWrapper<GraduationCommentTeach> queryWrapper, String student) {
        return this.baseMapper.selectCTPage(page, queryWrapper, student);
    }
}
