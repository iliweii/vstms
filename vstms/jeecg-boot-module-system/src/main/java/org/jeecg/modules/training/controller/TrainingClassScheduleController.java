package org.jeecg.modules.training.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.training.entity.TrainingClassSchedule;
import org.jeecg.modules.training.service.ITrainingClassScheduleService;
import org.jeecg.modules.training.service.ITrainingScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 课程表
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "课程表")
@RestController
@RequestMapping("/training/trainingClassSchedule")
public class TrainingClassScheduleController extends JeecgController<TrainingClassSchedule, ITrainingClassScheduleService> {
    @Autowired
    private ITrainingClassScheduleService trainingClassScheduleService;
    @Autowired
    private ITrainingScoreService trainingScoreService;

    /**
     * 分页列表查询
     *
     * @param trainingClassSchedule
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "课程表-分页列表查询")
    @ApiOperation(value = "课程表-分页列表查询", notes = "课程表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrainingClassSchedule trainingClassSchedule,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrainingClassSchedule> queryWrapper = QueryGenerator.initQueryWrapper(trainingClassSchedule, req.getParameterMap());
        // 培训班编号必传，否则返回空数据
        if (StringUtils.isEmpty(trainingClassSchedule.getClassNo())) {
            queryWrapper.eq("class_no", "-1");
        }
        Page<TrainingClassSchedule> page = new Page<TrainingClassSchedule>(pageNo, pageSize);
        IPage<TrainingClassSchedule> pageList = trainingClassScheduleService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trainingClassSchedule
     * @return
     */
    @AutoLog(value = "课程表-添加")
    @ApiOperation(value = "课程表-添加", notes = "课程表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrainingClassSchedule trainingClassSchedule) {
        trainingClassScheduleService.save(trainingClassSchedule);
        return Result.OK("添加成功！", trainingClassSchedule);
    }

    /**
     * 编辑
     *
     * @param trainingClassSchedule
     * @return
     */
    @AutoLog(value = "课程表-编辑")
    @ApiOperation(value = "课程表-编辑", notes = "课程表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrainingClassSchedule trainingClassSchedule) {
        // 编辑之前 校验是否已录入成绩，若已录入，禁止修改课程表名称
        TrainingClassSchedule scheduleQuery = this.trainingClassScheduleService.getById(trainingClassSchedule.getId());
        // 判断课程名称是否变化
        if (StringUtils.isNotEmpty(trainingClassSchedule.getCourseName())
                && !StringUtils.equals(trainingClassSchedule.getCourseName(), scheduleQuery.getCourseName())) {
            // 变化， 校验是否已录入成绩
            if (this.trainingScoreService.isEnterGrades(scheduleQuery.getClassNo(),
                    scheduleQuery.getCourseName())) {
                // 若已录入，禁止修改课程表名称
                return Result.error("该课程已录入成绩，不可修改课程名称");
            }
        }

        trainingClassScheduleService.updateById(trainingClassSchedule);
        return Result.OK("编辑成功!", trainingClassSchedule);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "课程表-通过id删除")
    @ApiOperation(value = "课程表-通过id删除", notes = "课程表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trainingClassScheduleService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "课程表-批量删除")
    @ApiOperation(value = "课程表-批量删除", notes = "课程表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trainingClassScheduleService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "课程表-通过id查询")
    @ApiOperation(value = "课程表-通过id查询", notes = "课程表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrainingClassSchedule trainingClassSchedule = trainingClassScheduleService.getById(id);
        return Result.OK(trainingClassSchedule);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trainingClassSchedule
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrainingClassSchedule trainingClassSchedule) {
        return super.exportXls(request, trainingClassSchedule, TrainingClassSchedule.class, "课程表");
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
        return super.importExcel(request, response, TrainingClassSchedule.class);
    }

}
