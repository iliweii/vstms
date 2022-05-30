package org.jeecg.modules.graduation.controller;

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
import org.jeecg.modules.graduation.entity.GraduationSelectStudent;
import org.jeecg.modules.graduation.service.IGraduationSelectStudentService;
import org.jeecg.modules.training.vo.TrainingLinkModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 评选优秀学员
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "评选优秀学员")
@RestController
@RequestMapping("/graduation/graduationSelectStudent")
public class GraduationSelectStudentController extends JeecgController<GraduationSelectStudent, IGraduationSelectStudentService> {
    @Autowired
    private IGraduationSelectStudentService graduationSelectStudentService;

    /**
     * 分页列表查询
     *
     * @param graduationSelectStudent
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "评选优秀学员-分页列表查询")
    @ApiOperation(value = "评选优秀学员-分页列表查询", notes = "评选优秀学员-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GraduationSelectStudent graduationSelectStudent, String type,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GraduationSelectStudent> queryWrapper = QueryGenerator.initQueryWrapper(graduationSelectStudent, req.getParameterMap());
        Page<GraduationSelectStudent> page = new Page<GraduationSelectStudent>(pageNo, pageSize);
        if (StringUtils.equals(type, "SelectionList")) {
            // 什么都不用做...
        } else if (StringUtils.equals(type, "ExcellentList")) {
            queryWrapper.eq("status", "1");
        }
        IPage<GraduationSelectStudent> pageList = graduationSelectStudentService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param graduationSelectStudent
     * @return
     */
    @AutoLog(value = "评选优秀学员-添加")
    @ApiOperation(value = "评选优秀学员-添加", notes = "评选优秀学员-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GraduationSelectStudent graduationSelectStudent) {
        graduationSelectStudentService.save(graduationSelectStudent);
        return Result.OK("添加成功！", graduationSelectStudent);
    }

    /**
     * 编辑
     *
     * @param graduationSelectStudent
     * @return
     */
    @AutoLog(value = "评选优秀学员-编辑")
    @ApiOperation(value = "评选优秀学员-编辑", notes = "评选优秀学员-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GraduationSelectStudent graduationSelectStudent) {
        graduationSelectStudentService.updateById(graduationSelectStudent);
        return Result.OK("编辑成功!", graduationSelectStudent);
    }

    /**
     * 关联
     *
     * @param trainingLinkModel
     * @return
     */
    @AutoLog(value = "评选优秀学员-关联")
    @ApiOperation(value = "评选优秀学员-关联", notes = "评选优秀学员-关联")
    @PutMapping(value = "/link")
    public Result<?> link(@RequestBody TrainingLinkModel trainingLinkModel) {
        graduationSelectStudentService.link(trainingLinkModel);
        return Result.OK("关联成功!");
    }

    /**
     * 关联
     *
     * @param trainingLinkModel
     * @return
     */
    @AutoLog(value = "评选优秀学员-关联")
    @ApiOperation(value = "评选优秀学员-关联", notes = "评选优秀学员-关联")
    @PutMapping(value = "/linkResult")
    public Result<?> linkResult(@RequestBody TrainingLinkModel trainingLinkModel) {
        graduationSelectStudentService.linkResult(trainingLinkModel);
        return Result.OK("关联成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "评选优秀学员-通过id删除")
    @ApiOperation(value = "评选优秀学员-通过id删除", notes = "评选优秀学员-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        graduationSelectStudentService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "评选优秀学员-批量删除")
    @ApiOperation(value = "评选优秀学员-批量删除", notes = "评选优秀学员-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.graduationSelectStudentService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "评选优秀学员-通过id查询")
    @ApiOperation(value = "评选优秀学员-通过id查询", notes = "评选优秀学员-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GraduationSelectStudent graduationSelectStudent = graduationSelectStudentService.getById(id);
        return Result.OK(graduationSelectStudent);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param graduationSelectStudent
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GraduationSelectStudent graduationSelectStudent) {
        return super.exportXls(request, graduationSelectStudent, GraduationSelectStudent.class, "评选优秀学员");
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
        return super.importExcel(request, response, GraduationSelectStudent.class);
    }

}
