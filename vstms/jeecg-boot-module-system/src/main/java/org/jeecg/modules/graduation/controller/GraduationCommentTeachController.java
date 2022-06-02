package org.jeecg.modules.graduation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.graduation.entity.GraduationCommentTeach;
import org.jeecg.modules.graduation.service.IGraduationCommentTeachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 评教
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "评教")
@RestController
@RequestMapping("/graduation/graduationCommentTeach")
public class GraduationCommentTeachController extends JeecgController<GraduationCommentTeach, IGraduationCommentTeachService> {
    @Autowired
    private IGraduationCommentTeachService graduationCommentTeachService;

    /**
     * 分页列表查询
     *
     * @param graduationCommentTeach
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "评教-分页列表查询")
    @ApiOperation(value = "评教-分页列表查询", notes = "评教-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GraduationCommentTeach graduationCommentTeach, String type,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GraduationCommentTeach> queryWrapper = QueryGenerator.initQueryWrapper(graduationCommentTeach, req.getParameterMap());
        Page<GraduationCommentTeach> page = new Page<GraduationCommentTeach>(pageNo, pageSize);
        IPage<GraduationCommentTeach> pageList;
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isNotEmpty(type) && StringUtils.equals("student", type)) {
            pageList = graduationCommentTeachService.studentCommentPage(page, queryWrapper, sysUser.getUsername());
        } else {
            pageList = graduationCommentTeachService.page(page, queryWrapper);

        }
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param graduationCommentTeach
     * @return
     */
    @AutoLog(value = "评教-添加")
    @ApiOperation(value = "评教-添加", notes = "评教-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GraduationCommentTeach graduationCommentTeach) {
        graduationCommentTeachService.save(graduationCommentTeach);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param graduationCommentTeach
     * @return
     */
    @AutoLog(value = "评教-编辑")
    @ApiOperation(value = "评教-编辑", notes = "评教-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GraduationCommentTeach graduationCommentTeach) {
        graduationCommentTeachService.updateById(graduationCommentTeach);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "评教-通过id删除")
    @ApiOperation(value = "评教-通过id删除", notes = "评教-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        graduationCommentTeachService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "评教-批量删除")
    @ApiOperation(value = "评教-批量删除", notes = "评教-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.graduationCommentTeachService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "评教-通过id查询")
    @ApiOperation(value = "评教-通过id查询", notes = "评教-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GraduationCommentTeach graduationCommentTeach = graduationCommentTeachService.getById(id);
        return Result.OK(graduationCommentTeach);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param graduationCommentTeach
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GraduationCommentTeach graduationCommentTeach) {
        return super.exportXls(request, graduationCommentTeach, GraduationCommentTeach.class, "评教");
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
        return super.importExcel(request, response, GraduationCommentTeach.class);
    }

}
