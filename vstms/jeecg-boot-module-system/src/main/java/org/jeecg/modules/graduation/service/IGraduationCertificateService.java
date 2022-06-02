package org.jeecg.modules.graduation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.graduation.entity.GraduationCertificate;

/**
 * @Description: 证书编号表
 * @Author: jeecg-boot
 * @Date: 2022-06-03
 * @Version: V1.0
 */
public interface IGraduationCertificateService extends IService<GraduationCertificate> {

    /**
     * 新增
     *
     * @param graduationCertificate
     */
    void add(GraduationCertificate graduationCertificate);

}
