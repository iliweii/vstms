package org.jeecg.modules.graduation.controller;

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
import org.jeecg.modules.graduation.entity.GraduationCeremony;
import org.jeecg.modules.graduation.service.IGraduationCeremonyService;

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
 * @Description: 结业典礼
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "结业典礼")
@RestController
@RequestMapping("/graduation/graduationCeremony")
public class GraduationCeremonyController extends JeecgController<GraduationCeremony, IGraduationCeremonyService> {
    @Autowired
    private IGraduationCeremonyService graduationCeremonyService;

    /**
     * 分页列表查询
     *
     * @param graduationCeremony
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "结业典礼-分页列表查询")
    @ApiOperation(value = "结业典礼-分页列表查询", notes = "结业典礼-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GraduationCeremony graduationCeremony,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GraduationCeremony> queryWrapper = QueryGenerator.initQueryWrapper(graduationCeremony, req.getParameterMap());
        Page<GraduationCeremony> page = new Page<GraduationCeremony>(pageNo, pageSize);
        IPage<GraduationCeremony> pageList = graduationCeremonyService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 编辑
     *
     * @param graduationCeremony
     * @return
     */
    @AutoLog(value = "结业典礼-编辑")
    @ApiOperation(value = "结业典礼-编辑", notes = "结业典礼-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GraduationCeremony graduationCeremony) {
        graduationCeremonyService.edit(graduationCeremony);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "结业典礼-通过id删除")
    @ApiOperation(value = "结业典礼-通过id删除", notes = "结业典礼-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        graduationCeremonyService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "结业典礼-批量删除")
    @ApiOperation(value = "结业典礼-批量删除", notes = "结业典礼-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.graduationCeremonyService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "结业典礼-通过id查询")
    @ApiOperation(value = "结业典礼-通过id查询", notes = "结业典礼-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GraduationCeremony graduationCeremony = graduationCeremonyService.getById(id);
        return Result.OK(graduationCeremony);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param graduationCeremony
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GraduationCeremony graduationCeremony) {
        return super.exportXls(request, graduationCeremony, GraduationCeremony.class, "结业典礼");
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
        return super.importExcel(request, response, GraduationCeremony.class);
    }

}
