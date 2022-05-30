package org.jeecg.modules.training.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.training.entity.TrainingScore;
import org.jeecg.modules.training.service.ITrainingScoreService;
import org.jeecg.modules.training.vo.TrainingCourseScoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Description: 成绩管理
 * @Author: jeecg-boot
 * @Date: 2022-05-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "成绩管理")
@RestController
@RequestMapping("/training/trainingScore")
public class TrainingScoreController extends JeecgController<TrainingScore, ITrainingScoreService> {
    @Autowired
    private ITrainingScoreService trainingScoreService;

    /**
     * 分页列表查询
     *
     * @param trainingScore
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "成绩管理-分页列表查询")
    @ApiOperation(value = "成绩管理-分页列表查询", notes = "成绩管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrainingScore trainingScore,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrainingScore> queryWrapper = QueryGenerator.initQueryWrapper(trainingScore, req.getParameterMap());
        Page<TrainingScore> page = new Page<TrainingScore>(pageNo, pageSize);
        IPage<TrainingScore> pageList = trainingScoreService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param trainingScore
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "成绩管理-分页列表查询")
    @ApiOperation(value = "成绩管理-分页列表查询", notes = "成绩管理-分页列表查询")
    @GetMapping(value = "/courseList")
    public Result<?> queryCoursePage(TrainingCourseScoreVO trainingScore,
                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest req) {
        QueryWrapper<TrainingCourseScoreVO> queryWrapper = QueryGenerator.initQueryWrapper(trainingScore, req.getParameterMap());
        Page<TrainingCourseScoreVO> page = new Page<TrainingCourseScoreVO>(pageNo, pageSize);
        IPage<TrainingCourseScoreVO> pageList = trainingScoreService.coursePage(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trainingScore
     * @return
     */
    @AutoLog(value = "成绩管理-添加")
    @ApiOperation(value = "成绩管理-添加", notes = "成绩管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrainingScore trainingScore) {
        trainingScoreService.save(trainingScore);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trainingScore
     * @return
     */
    @AutoLog(value = "成绩管理-编辑")
    @ApiOperation(value = "成绩管理-编辑", notes = "成绩管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrainingScore trainingScore) {
        trainingScoreService.updateById(trainingScore);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "成绩管理-通过id删除")
    @ApiOperation(value = "成绩管理-通过id删除", notes = "成绩管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trainingScoreService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "成绩管理-批量删除")
    @ApiOperation(value = "成绩管理-批量删除", notes = "成绩管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trainingScoreService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "成绩管理-通过id查询")
    @ApiOperation(value = "成绩管理-通过id查询", notes = "成绩管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrainingScore trainingScore = trainingScoreService.getById(id);
        return Result.OK(trainingScore);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trainingScore
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrainingScore trainingScore) {
        return super.exportXls(request, trainingScore, TrainingScore.class, "成绩管理");
    }

    /**
     * 下载导入模板
     */
    @RequestMapping(value = "/exportTemplate")
    public ModelAndView exportTemplate(@RequestParam(name = "classNo", required = true) String classNo,
                                       @RequestParam(name = "courseName", required = true) String courseName) {
        return this.trainingScoreService.exportTemplate(classNo, courseName);
    }

    /**
     * 下载错误信息
     */
    @RequestMapping(value = "/errorXls")
    public ModelAndView errorXls() {
        return this.trainingScoreService.errorXls();
    }

    /**
     * 通过excel导入数据
     *
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件流对象", required = true, dataType = "__File")})
    public Result<?> importExcel(@RequestParam(value = "file") MultipartFile multipartFile) {
        if (StringUtils.isEmpty(multipartFile.getOriginalFilename()))
            return Result.error("文件不存在");
        return this.trainingScoreService.importExcel(multipartFile);
    }

}
