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
import org.jeecg.modules.graduation.entity.GraduationSuggestions;
import org.jeecg.modules.graduation.service.IGraduationSuggestionsService;
import org.jeecg.modules.system.service.ISysNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 意见建议
 * @Author: jeecg-boot
 * @Date: 2022-06-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "意见建议")
@RestController
@RequestMapping("/graduation/graduationSuggestions")
public class GraduationSuggestionsController extends JeecgController<GraduationSuggestions, IGraduationSuggestionsService> {
    @Autowired
    private IGraduationSuggestionsService graduationSuggestionsService;
    @Autowired
    private ISysNoService sysNoService;

    /**
     * 分页列表查询
     *
     * @param graduationSuggestions
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "意见建议-分页列表查询")
    @ApiOperation(value = "意见建议-分页列表查询", notes = "意见建议-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GraduationSuggestions graduationSuggestions, String type,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GraduationSuggestions> queryWrapper = QueryGenerator.initQueryWrapper(graduationSuggestions, req.getParameterMap());
        Page<GraduationSuggestions> page = new Page<GraduationSuggestions>(pageNo, pageSize);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.equals("self", type)) {
            queryWrapper.eq("create_by", loginUser.getUsername());
        }
        IPage<GraduationSuggestions> pageList = graduationSuggestionsService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param graduationSuggestions
     * @return
     */
    @AutoLog(value = "意见建议-添加")
    @ApiOperation(value = "意见建议-添加", notes = "意见建议-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GraduationSuggestions graduationSuggestions) {
        graduationSuggestions.setNo(sysNoService.generateNo("YJ{YM}-" + graduationSuggestions.getObj() + "{3}"));
        graduationSuggestionsService.save(graduationSuggestions);
        return Result.OK("添加成功！", graduationSuggestions);
    }

    /**
     * 编辑
     *
     * @param graduationSuggestions
     * @return
     */
    @AutoLog(value = "意见建议-编辑")
    @ApiOperation(value = "意见建议-编辑", notes = "意见建议-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GraduationSuggestions graduationSuggestions) {
        graduationSuggestionsService.updateById(graduationSuggestions);
        return Result.OK("编辑成功!", graduationSuggestions);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "意见建议-通过id删除")
    @ApiOperation(value = "意见建议-通过id删除", notes = "意见建议-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        graduationSuggestionsService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "意见建议-批量删除")
    @ApiOperation(value = "意见建议-批量删除", notes = "意见建议-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.graduationSuggestionsService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "意见建议-通过id查询")
    @ApiOperation(value = "意见建议-通过id查询", notes = "意见建议-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GraduationSuggestions graduationSuggestions = graduationSuggestionsService.getById(id);
        return Result.OK(graduationSuggestions);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param graduationSuggestions
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GraduationSuggestions graduationSuggestions) {
        return super.exportXls(request, graduationSuggestions, GraduationSuggestions.class, "意见建议");
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
        return super.importExcel(request, response, GraduationSuggestions.class);
    }

}
