package org.jeecg.modules.graduation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jeecg.modules.graduation.entity.GraduationSelectClass;
import org.jeecg.modules.graduation.mapper.GraduationSelectClassMapper;
import org.jeecg.modules.graduation.service.IGraduationSelectClassService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 评选优秀学员
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GraduationSelectClassServiceImpl extends ServiceImpl<GraduationSelectClassMapper, GraduationSelectClass> implements IGraduationSelectClassService {

    @Override
    public void edit(GraduationSelectClass graduationSelectClass) {
        // 根据培训班编码确定一条数据
        GraduationSelectClass selectQuery = this.lambdaQuery().eq(GraduationSelectClass::getClassNo, graduationSelectClass.getClassNo()).one();
        if (ObjectUtils.isNotEmpty(selectQuery)) {
            // 编辑
            graduationSelectClass.setId(selectQuery.getId());
            this.updateById(graduationSelectClass);
        } else {
            // 新增
            graduationSelectClass.setId(null);
            this.save(graduationSelectClass);
        }
    }
}
