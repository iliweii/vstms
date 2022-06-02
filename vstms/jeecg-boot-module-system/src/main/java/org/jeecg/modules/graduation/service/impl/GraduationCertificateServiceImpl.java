package org.jeecg.modules.graduation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.graduation.entity.GraduationCertificate;
import org.jeecg.modules.graduation.mapper.GraduationCertificateMapper;
import org.jeecg.modules.graduation.service.IGraduationCertificateService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysNoService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.training.entity.TrainingClass;
import org.jeecg.modules.training.service.ITrainingClassService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 证书编号表
 * @Author: jeecg-boot
 * @Date:   2022-06-03
 * @Version: V1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GraduationCertificateServiceImpl extends ServiceImpl<GraduationCertificateMapper, GraduationCertificate> implements IGraduationCertificateService {

    private final ISysNoService sysNoService;
    private final ISysUserService sysUserService;
    private final ITrainingClassService trainingClassService;


    @Override
    public void add(GraduationCertificate graduationCertificate) {
        TrainingClass trainingClass = this.trainingClassService.getByNo(graduationCertificate.getClassNo());
        SysUser sysUser = this.sysUserService.getUserByName(graduationCertificate.getStudent());
        // 生成证书编号，保存冗余数据
        graduationCertificate.setSkillName(trainingClass.getSkillName())
                .setStudentName(sysUser.getRealname())
                .setNo(sysNoService.generateNo("ZS" + graduationCertificate.getClassNo() + "{3}"))
                .setStatus("1");

        this.save(graduationCertificate);
    }
}
