package org.jeecg.modules.training.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.training.entity.TrainingScore;
import org.jeecg.modules.training.excel.AttendanceSheetImport;
import org.jeecg.modules.training.excel.CourseScoreExport;
import org.jeecg.modules.training.excel.CourseScoreImport;
import org.jeecg.modules.training.mapper.TrainingScoreMapper;
import org.jeecg.modules.training.service.ITrainingClassScheduleService;
import org.jeecg.modules.training.service.ITrainingScoreService;
import org.jeecg.modules.training.vo.TrainingCourseScoreVO;
import org.jeecg.modules.utils.ErrorMsgUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: 成绩管理
 * @Author: jeecg-boot
 * @Date: 2022-05-30
 * @Version: V1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TrainingScoreServiceImpl extends ServiceImpl<TrainingScoreMapper, TrainingScore> implements ITrainingScoreService {

    private final ISysUserService sysUserService;
    private final ITrainingClassScheduleService scheduleService;
    private final RedisUtil redisUtil;

    @Override
    public IPage<TrainingCourseScoreVO> coursePage(Page<TrainingCourseScoreVO> page, QueryWrapper<TrainingCourseScoreVO> queryWrapper) {
        return this.baseMapper.selectCoursePage(page, queryWrapper);
    }

    @Override
    public boolean isEnterGrades(String classNo, String courseName) {
        int count = this.lambdaQuery().eq(TrainingScore::getClassNo, classNo)
                .eq(TrainingScore::getCourseName, courseName)
                .count();
        return count > 0;
    }

    @Override
    public ModelAndView exportTemplate(String classNo, String courseName) {
        // Step.1 组装查询条件
        // Step.2 获取导出数据
        List<CourseScoreExport> pageList = this.baseMapper.getCourseScore(classNo, courseName);
        // 过滤选中数据
        // 组装结果

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "成绩表"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, CourseScoreExport.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("成绩表", "成绩表上传模板，用于培训班编号：" + classNo + "，课程名称" + courseName + "的成绩上传。" +
                "请勿修改除成绩外的其他已有数据。其中成绩为数字保留两位小数。", "成绩表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    @Override
    public ModelAndView errorXls() {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<AttendanceSheetImport> errorList = (List<AttendanceSheetImport>) redisUtil.get("sys:errImport:course_score_" + user.getUsername());
        if (errorList == null) {
            throw new JeecgBootException("暂无错误信息");
        }
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "成绩表导入报错");
        mv.addObject(NormalExcelConstants.CLASS, AttendanceSheetImport.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("成绩表导入错误信息", "成绩表导入错误信息，请根据错误信息修改无误后，重新导入本文件", "成绩表导入错误信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, errorList);
        return mv;
    }

    @Override
    public Result<?> importExcel(MultipartFile multipartFile) {
        ImportParams params = new ImportParams();
        params.setTitleRows(2);
        params.setHeadRows(1);
        try {
            // Step.1 读取导入数据
            LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            List<CourseScoreImport> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), CourseScoreImport.class, params);
            long start = System.currentTimeMillis();

            // Step.2 对数据进行初步校验
            List<CourseScoreImport> rightList = list;
            List<CourseScoreImport> errorList = new LinkedList<>();
            rightList = checkData(rightList, errorList);

            // Step.3 将导入数据转换为新增数据
            List<TrainingScore> importList = new LinkedList<>();
            rightList.forEach(detailImport -> {
                TrainingScore trainingScore = new TrainingScore();
                BeanUtils.copyProperties(detailImport, trainingScore);
                importList.add(trainingScore);
            });

            // Step.4 新增数据入库
            importList.forEach(score -> {
                TrainingScore scoreQuery = this.lambdaQuery()
                        .eq(TrainingScore::getClassNo, score.getClassNo())
                        .eq(TrainingScore::getUsername, score.getUsername())
                        .eq(TrainingScore::getCourseName, score.getCourseName())
                        .one();
                if (ObjectUtils.isNotEmpty(scoreQuery)) this.baseMapper.updateById(score);
                else this.baseMapper.insert(score);
            });

            // Step.5 若有错误信息，存入缓存
            if (!CollectionUtils.isEmpty(errorList)) {
                redisUtil.set("sys:errImport:course_score_" + user.getUsername(), errorList, 10 * 60L);
            }

            // Step.6 返回导入结果
            log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
            return Result.importReturn(errorList.size(), rightList.size(), "请下载查看详情（10分钟内有效）", "/training/trainingScore/errorXls");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("文件导入失败:" + e.getMessage());
        } finally {
            try {
                multipartFile.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对数据进行校验
     *
     * @param rightList 数据列表
     * @param errorList 错误数据列表
     * @return 正确数据列表
     */
    private List<CourseScoreImport> checkData(List<CourseScoreImport> rightList, List<CourseScoreImport> errorList) {
        List<CourseScoreImport> tempList = new LinkedList<>();
        int len = rightList.size();

        // 特殊数据，提前加载
        List<String> courseList = new ArrayList<>();
        if (rightList.size() > 0) courseList = scheduleService.getCourseList(rightList.get(0).getClassNo());
        for (CourseScoreImport detailImport : rightList) {
            StringBuffer stringBuffer = new StringBuffer();

            ErrorMsgUtil.checkError(stringBuffer, detailImport.getClassNo(), "培训班编号不能为空；");
            ErrorMsgUtil.checkError(stringBuffer, detailImport.getUsername(), "学员姓名不能为空；");
            ErrorMsgUtil.checkError(stringBuffer, detailImport.getCourseName(), "课程名称不能为空；");
            ErrorMsgUtil.checkError(stringBuffer, this.sysUserService.getUserByName(detailImport.getUsername()), "未找到学员" + detailImport.getUsername() + "；");
            ErrorMsgUtil.checkError(stringBuffer, detailImport.getScore(), "成绩不能为空；");
            if (!courseList.contains(detailImport.getCourseName())) {
                ErrorMsgUtil.checkError(stringBuffer, true, "课程名称填写有误，请填写如下课程名称：" + String.join("、", courseList) + "；");
            }
            if (!StringUtils.isEmpty(stringBuffer.toString())) {
                detailImport.setMsg(stringBuffer.toString());
                errorList.add(detailImport);
            } else {
                tempList.add(detailImport);
            }
        }
        return tempList;
    }
}
