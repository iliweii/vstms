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
import org.jeecg.modules.tracking.entity.TrackingExpenditure;
import org.jeecg.modules.tracking.service.ITrackingExpenditureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 经费支出
 * @Author: jeecg-boot
 * @Date: 2022-06-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "经费支出")
@RestController
@RequestMapping("/tracking/trackingExpenditure")
public class TrackingExpenditureController extends JeecgController<TrackingExpenditure, ITrackingExpenditureService> {
    @Autowired
    private ITrackingExpenditureService trackingExpenditureService;

    /**
     * 分页列表查询
     *
     * @param trackingExpenditure
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "经费支出-分页列表查询")
    @ApiOperation(value = "经费支出-分页列表查询", notes = "经费支出-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrackingExpenditure trackingExpenditure,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrackingExpenditure> queryWrapper = QueryGenerator.initQueryWrapper(trackingExpenditure, req.getParameterMap());
        Page<TrackingExpenditure> page = new Page<TrackingExpenditure>(pageNo, pageSize);
        IPage<TrackingExpenditure> pageList = trackingExpenditureService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trackingExpenditure
     * @return
     */
    @AutoLog(value = "经费支出-添加")
    @ApiOperation(value = "经费支出-添加", notes = "经费支出-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrackingExpenditure trackingExpenditure) {
        trackingExpenditureService.save(trackingExpenditure);
        return Result.OK("添加成功！", trackingExpenditure);
    }

    /**
     * 编辑
     *
     * @param trackingExpenditure
     * @return
     */
    @AutoLog(value = "经费支出-编辑")
    @ApiOperation(value = "经费支出-编辑", notes = "经费支出-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrackingExpenditure trackingExpenditure) {
        trackingExpenditureService.updateById(trackingExpenditure);
        return Result.OK("编辑成功!", trackingExpenditure);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "经费支出-通过id删除")
    @ApiOperation(value = "经费支出-通过id删除", notes = "经费支出-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trackingExpenditureService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "经费支出-批量删除")
    @ApiOperation(value = "经费支出-批量删除", notes = "经费支出-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trackingExpenditureService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "经费支出-通过id查询")
    @ApiOperation(value = "经费支出-通过id查询", notes = "经费支出-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrackingExpenditure trackingExpenditure = trackingExpenditureService.getById(id);
        return Result.OK(trackingExpenditure);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trackingExpenditure
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrackingExpenditure trackingExpenditure) {
        return super.exportXls(request, trackingExpenditure, TrackingExpenditure.class, "经费支出");
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
        return super.importExcel(request, response, TrackingExpenditure.class);
    }

}
