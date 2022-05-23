package org.jeecg.modules.training.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.training.entity.TrainingReceive;
import org.jeecg.modules.training.service.ITrainingReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 资料领取
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "资料领取")
@RestController
@RequestMapping("/training/trainingReceive")
public class TrainingReceiveController extends JeecgController<TrainingReceive, ITrainingReceiveService> {
    @Autowired
    private ITrainingReceiveService trainingReceiveService;

    /**
     * 分页列表查询
     *
     * @param trainingReceive
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "资料领取-分页列表查询")
    @ApiOperation(value = "资料领取-分页列表查询", notes = "资料领取-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrainingReceive trainingReceive,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrainingReceive> queryWrapper = QueryGenerator.initQueryWrapper(trainingReceive, req.getParameterMap());
        Page<TrainingReceive> page = new Page<TrainingReceive>(pageNo, pageSize);
        IPage<TrainingReceive> pageList = trainingReceiveService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trainingReceive
     * @return
     */
    @AutoLog(value = "资料领取-添加")
    @ApiOperation(value = "资料领取-添加", notes = "资料领取-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrainingReceive trainingReceive) {
        trainingReceiveService.save(trainingReceive);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trainingReceive
     * @return
     */
    @AutoLog(value = "资料领取-编辑")
    @ApiOperation(value = "资料领取-编辑", notes = "资料领取-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrainingReceive trainingReceive) {
        trainingReceiveService.updateById(trainingReceive);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "资料领取-通过id删除")
    @ApiOperation(value = "资料领取-通过id删除", notes = "资料领取-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trainingReceiveService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "资料领取-批量删除")
    @ApiOperation(value = "资料领取-批量删除", notes = "资料领取-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trainingReceiveService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "资料领取-通过id查询")
    @ApiOperation(value = "资料领取-通过id查询", notes = "资料领取-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrainingReceive trainingReceive = trainingReceiveService.getById(id);
        return Result.OK(trainingReceive);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trainingReceive
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrainingReceive trainingReceive) {
        return super.exportXls(request, trainingReceive, TrainingReceive.class, "资料领取");
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
        return super.importExcel(request, response, TrainingReceive.class);
    }

}
