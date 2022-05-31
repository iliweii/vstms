package org.jeecg.modules.graduation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.graduation.entity.GraduationCeremony;

/**
 * @Description: 结业典礼
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
public interface IGraduationCeremonyService extends IService<GraduationCeremony> {

    /**
     * 编辑
     *
     * @param graduationCeremony
     */
    void edit(GraduationCeremony graduationCeremony);

}
