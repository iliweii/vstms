package org.jeecg.modules.graduation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jeecg.modules.graduation.entity.GraduationCeremony;
import org.jeecg.modules.graduation.mapper.GraduationCeremonyMapper;
import org.jeecg.modules.graduation.service.IGraduationCeremonyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 结业典礼
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GraduationCeremonyServiceImpl extends ServiceImpl<GraduationCeremonyMapper, GraduationCeremony> implements IGraduationCeremonyService {

    @Override
    public void edit(GraduationCeremony graduationCeremony) {
        // 根据培训班编码确定一条数据
        GraduationCeremony ceremonyQuery = this.lambdaQuery().eq(GraduationCeremony::getClassNo, graduationCeremony.getClassNo()).one();
        if (ObjectUtils.isNotEmpty(ceremonyQuery)) {
            // 编辑
            graduationCeremony.setId(ceremonyQuery.getId());
            this.updateById(graduationCeremony);
        } else {
            // 新增
            graduationCeremony.setId(null);
            this.save(graduationCeremony);
        }
    }
}
