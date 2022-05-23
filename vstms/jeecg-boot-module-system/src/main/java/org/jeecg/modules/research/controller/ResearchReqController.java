package org.jeecg.modules.research.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.research.entity.ResearchReq;
import org.jeecg.modules.research.service.IResearchReqService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: 需求调研
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "需求调研")
@RestController
@RequestMapping("/research/researchReq")
public class ResearchReqController extends JeecgController<ResearchReq, IResearchReqService> {
    @Autowired
    private IResearchReqService researchReqService;

    /**
     * 分页列表查询
     *
     * @param researchReq
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "需求调研-分页列表查询")
    @ApiOperation(value = "需求调研-分页列表查询", notes = "需求调研-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ResearchReq researchReq,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ResearchReq> queryWrapper = QueryGenerator.initQueryWrapper(researchReq, req.getParameterMap());
        Page<ResearchReq> page = new Page<ResearchReq>(pageNo, pageSize);
        IPage<ResearchReq> pageList = researchReqService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param researchReq
     * @return
     */
    @AutoLog(value = "需求调研-添加")
    @ApiOperation(value = "需求调研-添加", notes = "需求调研-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ResearchReq researchReq) {
        researchReqService.add(researchReq);
        return Result.OK("添加成功！", researchReq);
    }

    /**
     * 编辑
     *
     * @param researchReq
     * @return
     */
    @AutoLog(value = "需求调研-编辑")
    @ApiOperation(value = "需求调研-编辑", notes = "需求调研-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ResearchReq researchReq) {
        researchReqService.updateById(researchReq);
        return Result.OK("编辑成功!", researchReq);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "需求调研-通过id删除")
    @ApiOperation(value = "需求调研-通过id删除", notes = "需求调研-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        researchReqService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "需求调研-批量删除")
    @ApiOperation(value = "需求调研-批量删除", notes = "需求调研-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.researchReqService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "需求调研-通过id查询")
    @ApiOperation(value = "需求调研-通过id查询", notes = "需求调研-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ResearchReq researchReq = researchReqService.getById(id);
        return Result.OK(researchReq);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param researchReq
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ResearchReq researchReq) {
        return super.exportXls(request, researchReq, ResearchReq.class, "需求调研");
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
        return super.importExcel(request, response, ResearchReq.class);
    }

}
