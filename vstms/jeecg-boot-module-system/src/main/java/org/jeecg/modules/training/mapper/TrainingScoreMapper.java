package org.jeecg.modules.training.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.training.entity.TrainingScore;
import org.jeecg.modules.training.excel.CourseScoreExport;
import org.jeecg.modules.training.vo.TrainingCourseScoreVO;

import java.util.List;

/**
 * @Description: 成绩管理
 * @Author: jeecg-boot
 * @Date: 2022-05-30
 * @Version: V1.0
 */
public interface TrainingScoreMapper extends BaseMapper<TrainingScore> {

    /**
     * 分页查询
     *
     * @param page         分页参数
     * @param queryWrapper 查询构造器
     * @return 分页结果
     */
    IPage<TrainingCourseScoreVO> selectCoursePage(Page<TrainingCourseScoreVO> page, @Param(Constants.WRAPPER) QueryWrapper<TrainingCourseScoreVO> queryWrapper);

    /**
     * 查询培训班和课程名称的成绩情况导出列表
     *
     * @param classNo
     * @param courseName
     * @return
     */
    List<CourseScoreExport> getCourseScore(String classNo, String courseName);

}
