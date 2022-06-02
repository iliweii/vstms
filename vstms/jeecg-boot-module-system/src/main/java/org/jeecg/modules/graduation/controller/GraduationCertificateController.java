package org.jeecg.modules.graduation.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.graduation.entity.GraduationCertificate;
import org.jeecg.modules.graduation.service.IGraduationCertificateService;
import org.jeecg.modules.training.entity.TrainingScore;
import org.jeecg.modules.training.service.ITrainingScoreService;
import org.jeecg.modules.training.vo.TrainingCourseScoreVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 证书编号表
 * @Author: jeecg-boot
 * @Date: 2022-06-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "证书编号表")
@RestController
@RequestMapping("/graduation/graduationCertificate")
@RequiredArgsConstructor
public class GraduationCertificateController extends JeecgController<GraduationCertificate, IGraduationCertificateService> {

    private final IGraduationCertificateService graduationCertificateService;
    private final ITrainingScoreService trainingScoreService;

    /**
     * 分页列表查询
     *
     * @param graduationCertificate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "证书编号表-分页列表查询")
    @ApiOperation(value = "证书编号表-分页列表查询", notes = "证书编号表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GraduationCertificate graduationCertificate,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GraduationCertificate> queryWrapper = QueryGenerator.initQueryWrapper(graduationCertificate, req.getParameterMap());
        Page<GraduationCertificate> page = new Page<GraduationCertificate>(pageNo, pageSize);
        IPage<GraduationCertificate> pageList = graduationCertificateService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param graduationCertificate
     * @return
     */
    @AutoLog(value = "证书编号表-添加")
    @ApiOperation(value = "证书编号表-添加", notes = "证书编号表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GraduationCertificate graduationCertificate) {
        graduationCertificateService.add(graduationCertificate);
        return Result.OK("添加成功！", graduationCertificate);
    }

    /**
     * 编辑
     *
     * @param graduationCertificate
     * @return
     */
    @AutoLog(value = "证书编号表-编辑")
    @ApiOperation(value = "证书编号表-编辑", notes = "证书编号表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GraduationCertificate graduationCertificate) {
        graduationCertificateService.updateById(graduationCertificate);
        return Result.OK("编辑成功!", graduationCertificate);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "证书编号表-通过id删除")
    @ApiOperation(value = "证书编号表-通过id删除", notes = "证书编号表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        graduationCertificateService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "证书编号表-批量删除")
    @ApiOperation(value = "证书编号表-批量删除", notes = "证书编号表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.graduationCertificateService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "证书编号表-通过id查询")
    @ApiOperation(value = "证书编号表-通过id查询", notes = "证书编号表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GraduationCertificate graduationCertificate = graduationCertificateService.getById(id);
        return Result.OK(graduationCertificate);
    }

    /**
     * 通过id查询
     *
     * @return
     */
    @AutoLog(value = "证书编号表-通过id查询")
    @ApiOperation(value = "证书编号表-通过id查询", notes = "证书编号表-通过id查询")
    @GetMapping(value = "/queryByStudent")
    public Result<?> queryByStudent(@RequestParam(name = "classNo") String classNo,
                                    @RequestParam(name = "student") String student) {
        GraduationCertificate graduationCertificate = graduationCertificateService.lambdaQuery()
                .eq(GraduationCertificate::getClassNo, classNo)
                .eq(GraduationCertificate::getStatus, student)
                .one();
        JSONObject result = new JSONObject(5);
        result.put("object", graduationCertificate);
        // 课程数，已参加考试课程数，平均成绩，成绩列表
        List<TrainingScore> scoreListByStudent = trainingScoreService.getScoreListByStudent(classNo, student);
        long courseNum = trainingScoreService.coursePage(new Page<TrainingCourseScoreVO>(1, 10), new QueryWrapper<>()).getTotal();
        int examNum = scoreListByStudent.size();
        BigDecimal sumScore = BigDecimal.ZERO;
        for (TrainingScore score : scoreListByStudent) {
            sumScore = sumScore.add(score.getScore());
        }
        BigDecimal avgScore;
        if (examNum != 0) avgScore = sumScore.divide(BigDecimal.valueOf(examNum), 2, BigDecimal.ROUND_UP);
        else avgScore = BigDecimal.ZERO;
        result.put("score", scoreListByStudent);
        result.put("courseNum", courseNum);
        result.put("examNum", examNum);
        result.put("sumScore", sumScore);
        result.put("avgScore", avgScore);
        return Result.OK(result);
    }

    /**
     * 查询证书
     *
     * @return
     */
    @AutoLog(value = "证书编号表-查询证书")
    @ApiOperation(value = "证书编号表-查询证书", notes = "证书编号表-查询证书")
    @GetMapping(value = "/queryCert")
    public Result<?> queryCert(@RequestParam(name = "classNo") String classNo,
                                    @RequestParam(name = "student") String student) {
        GraduationCertificate graduationCertificate = graduationCertificateService.lambdaQuery()
                .eq(GraduationCertificate::getClassNo, classNo)
                .eq(GraduationCertificate::getStatus, student)
                .one();
        JSONObject result = new JSONObject(6);
        result.put("object", graduationCertificate);
        // 课程数，已参加考试课程数，平均成绩，成绩列表
        List<TrainingScore> scoreListByStudent = trainingScoreService.getScoreListByStudent(classNo, student);
        long courseNum = trainingScoreService.coursePage(new Page<TrainingCourseScoreVO>(1, 10), new QueryWrapper<>()).getTotal();
        int examNum = scoreListByStudent.size();
        BigDecimal sumScore = BigDecimal.ZERO;
        for (TrainingScore score : scoreListByStudent) {
            sumScore = sumScore.add(score.getScore());
        }
        BigDecimal avgScore;
        if (examNum != 0) avgScore = sumScore.divide(BigDecimal.valueOf(examNum), 2, BigDecimal.ROUND_UP);
        else avgScore = BigDecimal.ZERO;
        result.put("score", scoreListByStudent);
        result.put("courseNum", courseNum);
        result.put("examNum", examNum);
        result.put("sumScore", sumScore);
        result.put("avgScore", avgScore);
        return Result.OK(result);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param graduationCertificate
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GraduationCertificate graduationCertificate) {
        return super.exportXls(request, graduationCertificate, GraduationCertificate.class, "证书编号表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, GraduationCertificate.class);
    }

}
