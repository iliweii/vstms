package org.jeecg.modules.training.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.training.entity.TrainingSummary;
import org.jeecg.modules.training.service.ITrainingSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 培训总结表
 * @Author: jeecg-boot
 * @Date: 2022-05-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "培训总结表")
@RestController
@RequestMapping("/training/trainingSummary")
public class TrainingSummaryController extends JeecgController<TrainingSummary, ITrainingSummaryService> {
    @Autowired
    private ITrainingSummaryService trainingSummaryService;

    /**
     * 分页列表查询
     *
     * @param trainingSummary
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "培训总结表-分页列表查询")
    @ApiOperation(value = "培训总结表-分页列表查询", notes = "培训总结表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrainingSummary trainingSummary, String flag,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // flag 特殊标识 代表只能查询自己的日志
        if ("1".equals(flag)) {
            trainingSummary.setCreateBy(loginUser.getUsername());
        }
        QueryWrapper<TrainingSummary> queryWrapper = QueryGenerator.initQueryWrapper(trainingSummary, req.getParameterMap());
        Page<TrainingSummary> page = new Page<TrainingSummary>(pageNo, pageSize);
        IPage<TrainingSummary> pageList = trainingSummaryService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trainingSummary
     * @return
     */
    @AutoLog(value = "培训总结表-添加")
    @ApiOperation(value = "培训总结表-添加", notes = "培训总结表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrainingSummary trainingSummary) {
        // 编号逻辑，总结类型包括：
        // 学员感言      [username+classNo+'testimonials']
        // 学员培训总结   [username+classNo+'student']
        // 教师自评报告   [username+classNo+'teacher']
        // 培训典型案例   [classNo+'typicalCase']
        // 督导总结      [classNo+'supervision']
        // 培训班总结     [classNo]
        trainingSummaryService.save(trainingSummary);
        return Result.OK("添加成功！", trainingSummary);
    }

    /**
     * 编辑
     *
     * @param trainingSummary
     * @return
     */
    @AutoLog(value = "培训总结表-编辑")
    @ApiOperation(value = "培训总结表-编辑", notes = "培训总结表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrainingSummary trainingSummary) {
        trainingSummaryService.updateById(trainingSummary);
        return Result.OK("编辑成功!", trainingSummary);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "培训总结表-通过id删除")
    @ApiOperation(value = "培训总结表-通过id删除", notes = "培训总结表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trainingSummaryService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "培训总结表-批量删除")
    @ApiOperation(value = "培训总结表-批量删除", notes = "培训总结表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trainingSummaryService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "培训总结表-通过id查询")
    @ApiOperation(value = "培训总结表-通过id查询", notes = "培训总结表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrainingSummary trainingSummary = trainingSummaryService.getById(id);
        return Result.OK(trainingSummary);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trainingSummary
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrainingSummary trainingSummary) {
        return super.exportXls(request, trainingSummary, TrainingSummary.class, "培训总结表");
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
        return super.importExcel(request, response, TrainingSummary.class);
    }

}
