package org.jeecg.modules.graduation.controller;

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
import org.jeecg.modules.graduation.entity.GraduationSelectClass;
import org.jeecg.modules.graduation.service.IGraduationSelectClassService;
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
@RequestMapping("/graduation/graduationSelectClass")
public class GraduationSelectClassController extends JeecgController<GraduationSelectClass, IGraduationSelectClassService> {
    @Autowired
    private IGraduationSelectClassService graduationSelectClassService;

    /**
     * 分页列表查询
     *
     * @param graduationSelectClass
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "评选优秀学员-分页列表查询")
    @ApiOperation(value = "评选优秀学员-分页列表查询", notes = "评选优秀学员-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GraduationSelectClass graduationSelectClass,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GraduationSelectClass> queryWrapper = QueryGenerator.initQueryWrapper(graduationSelectClass, req.getParameterMap());
        Page<GraduationSelectClass> page = new Page<GraduationSelectClass>(pageNo, pageSize);
        IPage<GraduationSelectClass> pageList = graduationSelectClassService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 编辑
     *
     * @param graduationSelectClass
     * @return
     */
    @AutoLog(value = "评选优秀学员-编辑")
    @ApiOperation(value = "评选优秀学员-编辑", notes = "评选优秀学员-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GraduationSelectClass graduationSelectClass) {
        graduationSelectClassService.edit(graduationSelectClass);
        return Result.OK("编辑成功!");
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
        graduationSelectClassService.removeById(id);
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
        this.graduationSelectClassService.removeByIds(Arrays.asList(ids.split(",")));
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
        GraduationSelectClass graduationSelectClass = graduationSelectClassService.getById(id);
        return Result.OK(graduationSelectClass);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param graduationSelectClass
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GraduationSelectClass graduationSelectClass) {
        return super.exportXls(request, graduationSelectClass, GraduationSelectClass.class, "评选优秀学员");
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
        return super.importExcel(request, response, GraduationSelectClass.class);
    }

}
