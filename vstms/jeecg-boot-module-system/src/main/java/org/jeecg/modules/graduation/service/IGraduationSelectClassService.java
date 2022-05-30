package org.jeecg.modules.graduation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.graduation.entity.GraduationSelectClass;

/**
 * @Description: 评选优秀学员
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
public interface IGraduationSelectClassService extends IService<GraduationSelectClass> {

    /**
     * 编辑
     *
     * @param graduationSelectClass
     */
    void edit(GraduationSelectClass graduationSelectClass);

}
