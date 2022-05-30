package org.jeecg.modules.training.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.training.entity.TrainingScore;
import org.jeecg.modules.training.vo.TrainingCourseScoreVO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 成绩管理
 * @Author: jeecg-boot
 * @Date: 2022-05-30
 * @Version: V1.0
 */
public interface ITrainingScoreService extends IService<TrainingScore> {

    /**
     * 课程录取情况列表
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<TrainingCourseScoreVO> coursePage(Page<TrainingCourseScoreVO> page, QueryWrapper<TrainingCourseScoreVO> queryWrapper);

    /**
     * 是否已录入成绩
     *
     * @param classNo
     * @param courseName
     * @return 若已录入，返回true，否则返回false
     */
    boolean isEnterGrades(String classNo, String courseName);

    /**
     * 下载导入模板
     *
     * @return
     */
    ModelAndView exportTemplate(String classNo, String courseName);

    /**
     * 下载错误信息
     *
     * @return
     */
    ModelAndView errorXls();

    /**
     * 导入
     *
     * @param multipartFile
     * @return
     */
    Result<?> importExcel(MultipartFile multipartFile);

}
