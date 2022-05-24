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
import org.jeecg.modules.training.entity.TrainingLog;
import org.jeecg.modules.training.service.ITrainingLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 培训日志
 * @Author: jeecg-boot
 * @Date: 2022-05-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "培训日志")
@RestController
@RequestMapping("/training/trainingLog")
public class TrainingLogController extends JeecgController<TrainingLog, ITrainingLogService> {
    @Autowired
    private ITrainingLogService trainingLogService;

    /**
     * 分页列表查询
     *
     * @param trainingLog
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "培训日志-分页列表查询")
    @ApiOperation(value = "培训日志-分页列表查询", notes = "培训日志-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrainingLog trainingLog, String flag,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // flag 特殊标识 代表只能查询自己的日志
        if ("1".equals(flag)) {
            trainingLog.setCreateBy(loginUser.getUsername());
        }
        QueryWrapper<TrainingLog> queryWrapper = QueryGenerator.initQueryWrapper(trainingLog, req.getParameterMap());
        Page<TrainingLog> page = new Page<TrainingLog>(pageNo, pageSize);
        IPage<TrainingLog> pageList = trainingLogService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trainingLog
     * @return
     */
    @AutoLog(value = "培训日志-添加")
    @ApiOperation(value = "培训日志-添加", notes = "培训日志-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrainingLog trainingLog) {
        trainingLogService.add(trainingLog);
        return Result.OK("添加成功！", trainingLog);
    }

    /**
     * 编辑
     *
     * @param trainingLog
     * @return
     */
    @AutoLog(value = "培训日志-编辑")
    @ApiOperation(value = "培训日志-编辑", notes = "培训日志-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrainingLog trainingLog) {
        trainingLogService.updateById(trainingLog);
        return Result.OK("编辑成功!", trainingLog);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "培训日志-通过id删除")
    @ApiOperation(value = "培训日志-通过id删除", notes = "培训日志-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trainingLogService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "培训日志-批量删除")
    @ApiOperation(value = "培训日志-批量删除", notes = "培训日志-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trainingLogService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "培训日志-通过id查询")
    @ApiOperation(value = "培训日志-通过id查询", notes = "培训日志-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrainingLog trainingLog = trainingLogService.getById(id);
        return Result.OK(trainingLog);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trainingLog
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrainingLog trainingLog) {
        return super.exportXls(request, trainingLog, TrainingLog.class, "培训日志");
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
        return super.importExcel(request, response, TrainingLog.class);
    }

}
