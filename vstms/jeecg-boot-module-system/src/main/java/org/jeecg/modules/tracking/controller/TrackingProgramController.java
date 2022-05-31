package org.jeecg.modules.tracking.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.tracking.entity.TrackingProgram;
import org.jeecg.modules.tracking.service.ITrackingProgramService;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: 训后跟踪
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "训后跟踪")
@RestController
@RequestMapping("/tracking/trackingProgram")
public class TrackingProgramController extends JeecgController<TrackingProgram, ITrackingProgramService> {
    @Autowired
    private ITrackingProgramService trackingProgramService;

    /**
     * 分页列表查询
     *
     * @param trackingProgram
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "训后跟踪-分页列表查询")
    @ApiOperation(value = "训后跟踪-分页列表查询", notes = "训后跟踪-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrackingProgram trackingProgram,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrackingProgram> queryWrapper = QueryGenerator.initQueryWrapper(trackingProgram, req.getParameterMap());
        Page<TrackingProgram> page = new Page<TrackingProgram>(pageNo, pageSize);
        IPage<TrackingProgram> pageList = trackingProgramService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trackingProgram
     * @return
     */
    @AutoLog(value = "训后跟踪-添加")
    @ApiOperation(value = "训后跟踪-添加", notes = "训后跟踪-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrackingProgram trackingProgram) {
        trackingProgramService.add(trackingProgram);
        return Result.OK("添加成功！", trackingProgram);
    }

    /**
     * 编辑
     *
     * @param trackingProgram
     * @return
     */
    @AutoLog(value = "训后跟踪-编辑")
    @ApiOperation(value = "训后跟踪-编辑", notes = "训后跟踪-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrackingProgram trackingProgram) {
        trackingProgramService.updateById(trackingProgram);
        return Result.OK("编辑成功!", trackingProgram);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "训后跟踪-通过id删除")
    @ApiOperation(value = "训后跟踪-通过id删除", notes = "训后跟踪-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trackingProgramService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "训后跟踪-批量删除")
    @ApiOperation(value = "训后跟踪-批量删除", notes = "训后跟踪-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trackingProgramService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "训后跟踪-通过id查询")
    @ApiOperation(value = "训后跟踪-通过id查询", notes = "训后跟踪-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrackingProgram trackingProgram = trackingProgramService.getById(id);
        return Result.OK(trackingProgram);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trackingProgram
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrackingProgram trackingProgram) {
        return super.exportXls(request, trackingProgram, TrackingProgram.class, "训后跟踪");
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
        return super.importExcel(request, response, TrackingProgram.class);
    }

}
