package org.jeecg.modules.graduation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.graduation.entity.GraduationSelectStudent;
import org.jeecg.modules.training.vo.TrainingLinkModel;

/**
 * @Description: 评选优秀学员
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
public interface IGraduationSelectStudentService extends IService<GraduationSelectStudent> {

    /**
     * 编辑
     *
     * @param trainingLinkModel
     */
    void link(TrainingLinkModel trainingLinkModel);

    /**
     * 编辑
     *
     * @param trainingLinkModel
     */
    void linkResult(TrainingLinkModel trainingLinkModel);

}
