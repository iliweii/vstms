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
import org.jeecg.modules.training.entity.TrainingOpeningCeremony;
import org.jeecg.modules.training.service.ITrainingOpeningCeremonyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 开班典礼
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "开班典礼")
@RestController
@RequestMapping("/training/trainingOpeningCeremony")
public class TrainingOpeningCeremonyController extends JeecgController<TrainingOpeningCeremony, ITrainingOpeningCeremonyService> {
    @Autowired
    private ITrainingOpeningCeremonyService trainingOpeningCeremonyService;

    /**
     * 分页列表查询
     *
     * @param trainingOpeningCeremony
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "开班典礼-分页列表查询")
    @ApiOperation(value = "开班典礼-分页列表查询", notes = "开班典礼-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrainingOpeningCeremony trainingOpeningCeremony,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrainingOpeningCeremony> queryWrapper = QueryGenerator.initQueryWrapper(trainingOpeningCeremony, req.getParameterMap());
        Page<TrainingOpeningCeremony> page = new Page<TrainingOpeningCeremony>(pageNo, pageSize);
        IPage<TrainingOpeningCeremony> pageList = trainingOpeningCeremonyService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 编辑
     *
     * @param trainingOpeningCeremony
     * @return
     */
    @AutoLog(value = "开班典礼-编辑")
    @ApiOperation(value = "开班典礼-编辑", notes = "开班典礼-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrainingOpeningCeremony trainingOpeningCeremony) {
        trainingOpeningCeremonyService.edit(trainingOpeningCeremony);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "开班典礼-通过id删除")
    @ApiOperation(value = "开班典礼-通过id删除", notes = "开班典礼-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trainingOpeningCeremonyService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "开班典礼-批量删除")
    @ApiOperation(value = "开班典礼-批量删除", notes = "开班典礼-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trainingOpeningCeremonyService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "开班典礼-通过id查询")
    @ApiOperation(value = "开班典礼-通过id查询", notes = "开班典礼-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrainingOpeningCeremony trainingOpeningCeremony = trainingOpeningCeremonyService.getById(id);
        return Result.OK(trainingOpeningCeremony);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trainingOpeningCeremony
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrainingOpeningCeremony trainingOpeningCeremony) {
        return super.exportXls(request, trainingOpeningCeremony, TrainingOpeningCeremony.class, "开班典礼");
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
        return super.importExcel(request, response, TrainingOpeningCeremony.class);
    }

}
