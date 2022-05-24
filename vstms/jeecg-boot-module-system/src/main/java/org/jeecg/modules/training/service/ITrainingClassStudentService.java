package org.jeecg.modules.training.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.training.entity.TrainingClassStudent;
import org.jeecg.modules.training.vo.TrainingClassStudentVO;
import org.jeecg.modules.training.vo.TrainingLinkModel;

/**
 * @Description: 培训班学生关系
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
public interface ITrainingClassStudentService extends IService<TrainingClassStudent> {

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类
     */
    IPage<TrainingClassStudentVO> page(Page<TrainingClassStudentVO> page, QueryWrapper<TrainingClassStudentVO> queryWrapper);

    /**
     * 编辑
     *
     * @param trainingLinkModel
     */
    void edit(TrainingLinkModel trainingLinkModel);

}
