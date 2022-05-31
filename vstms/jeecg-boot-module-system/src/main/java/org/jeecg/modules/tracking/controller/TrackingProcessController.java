package org.jeecg.modules.tracking.controller;

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
import org.jeecg.modules.tracking.entity.TrackingProcess;
import org.jeecg.modules.tracking.service.ITrackingProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 训后跟踪过程
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "训后跟踪过程")
@RestController
@RequestMapping("/tracking/trackingProcess")
public class TrackingProcessController extends JeecgController<TrackingProcess, ITrackingProcessService> {
    @Autowired
    private ITrackingProcessService trackingProcessService;

    /**
     * 分页列表查询
     *
     * @param trackingProcess
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "训后跟踪过程-分页列表查询")
    @ApiOperation(value = "训后跟踪过程-分页列表查询", notes = "训后跟踪过程-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrackingProcess trackingProcess,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrackingProcess> queryWrapper = QueryGenerator.initQueryWrapper(trackingProcess, req.getParameterMap());
        Page<TrackingProcess> page = new Page<TrackingProcess>(pageNo, pageSize);
        IPage<TrackingProcess> pageList = trackingProcessService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trackingProcess
     * @return
     */
    @AutoLog(value = "训后跟踪过程-添加")
    @ApiOperation(value = "训后跟踪过程-添加", notes = "训后跟踪过程-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrackingProcess trackingProcess) {
        trackingProcessService.save(trackingProcess);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trackingProcess
     * @return
     */
    @AutoLog(value = "训后跟踪过程-编辑")
    @ApiOperation(value = "训后跟踪过程-编辑", notes = "训后跟踪过程-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrackingProcess trackingProcess) {
        trackingProcessService.updateById(trackingProcess);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "训后跟踪过程-通过id删除")
    @ApiOperation(value = "训后跟踪过程-通过id删除", notes = "训后跟踪过程-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trackingProcessService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "训后跟踪过程-批量删除")
    @ApiOperation(value = "训后跟踪过程-批量删除", notes = "训后跟踪过程-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trackingProcessService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "训后跟踪过程-通过id查询")
    @ApiOperation(value = "训后跟踪过程-通过id查询", notes = "训后跟踪过程-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrackingProcess trackingProcess = trackingProcessService.getById(id);
        return Result.OK(trackingProcess);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trackingProcess
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrackingProcess trackingProcess) {
        return super.exportXls(request, trackingProcess, TrackingProcess.class, "训后跟踪过程");
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
        return super.importExcel(request, response, TrackingProcess.class);
    }

}
