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
import org.jeecg.modules.tracking.entity.TrackingSupervision;
import org.jeecg.modules.tracking.service.ITrackingSupervisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 视导记录
 * @Author: jeecg-boot
 * @Date: 2022-06-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "视导记录")
@RestController
@RequestMapping("/tracking/trackingSupervision")
public class TrackingSupervisionController extends JeecgController<TrackingSupervision, ITrackingSupervisionService> {
    @Autowired
    private ITrackingSupervisionService trackingSupervisionService;

    /**
     * 分页列表查询
     *
     * @param trackingSupervision
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "视导记录-分页列表查询")
    @ApiOperation(value = "视导记录-分页列表查询", notes = "视导记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrackingSupervision trackingSupervision,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrackingSupervision> queryWrapper = QueryGenerator.initQueryWrapper(trackingSupervision, req.getParameterMap());
        Page<TrackingSupervision> page = new Page<TrackingSupervision>(pageNo, pageSize);
        IPage<TrackingSupervision> pageList = trackingSupervisionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trackingSupervision
     * @return
     */
    @AutoLog(value = "视导记录-添加")
    @ApiOperation(value = "视导记录-添加", notes = "视导记录-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrackingSupervision trackingSupervision) {
        trackingSupervisionService.save(trackingSupervision);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trackingSupervision
     * @return
     */
    @AutoLog(value = "视导记录-编辑")
    @ApiOperation(value = "视导记录-编辑", notes = "视导记录-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrackingSupervision trackingSupervision) {
        trackingSupervisionService.updateById(trackingSupervision);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "视导记录-通过id删除")
    @ApiOperation(value = "视导记录-通过id删除", notes = "视导记录-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trackingSupervisionService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "视导记录-批量删除")
    @ApiOperation(value = "视导记录-批量删除", notes = "视导记录-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trackingSupervisionService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "视导记录-通过id查询")
    @ApiOperation(value = "视导记录-通过id查询", notes = "视导记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrackingSupervision trackingSupervision = trackingSupervisionService.getById(id);
        return Result.OK(trackingSupervision);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trackingSupervision
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrackingSupervision trackingSupervision) {
        return super.exportXls(request, trackingSupervision, TrackingSupervision.class, "视导记录");
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
        return super.importExcel(request, response, TrackingSupervision.class);
    }

}
