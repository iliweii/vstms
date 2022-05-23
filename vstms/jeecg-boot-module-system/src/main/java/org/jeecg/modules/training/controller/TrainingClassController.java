package org.jeecg.modules.training.controller;

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
import org.jeecg.modules.training.entity.TrainingClass;
import org.jeecg.modules.training.service.ITrainingClassService;
import org.jeecg.modules.training.vo.TrainingClassVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 培训班
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "培训班")
@RestController
@RequestMapping("/training/trainingClass")
public class TrainingClassController extends JeecgController<TrainingClass, ITrainingClassService> {
    @Autowired
    private ITrainingClassService trainingClassService;

    /**
     * 分页列表查询
     *
     * @param trainingClass
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "培训班-分页列表查询")
    @ApiOperation(value = "培训班-分页列表查询", notes = "培训班-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrainingClassVO trainingClass,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrainingClassVO> queryWrapper = QueryGenerator.initQueryWrapper(trainingClass, req.getParameterMap());
        Page<TrainingClassVO> page = new Page<TrainingClassVO>(pageNo, pageSize);
        IPage<TrainingClassVO> pageList = trainingClassService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trainingClass
     * @return
     */
    @AutoLog(value = "培训班-添加")
    @ApiOperation(value = "培训班-添加", notes = "培训班-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrainingClass trainingClass) {
        trainingClassService.add(trainingClass);
        return Result.OK("添加成功！", trainingClass);
    }

    /**
     * 编辑
     *
     * @param trainingClass
     * @return
     */
    @AutoLog(value = "培训班-编辑")
    @ApiOperation(value = "培训班-编辑", notes = "培训班-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrainingClass trainingClass) {
        trainingClassService.updateById(trainingClass);
        return Result.OK("编辑成功!", trainingClass);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "培训班-通过id删除")
    @ApiOperation(value = "培训班-通过id删除", notes = "培训班-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trainingClassService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "培训班-批量删除")
    @ApiOperation(value = "培训班-批量删除", notes = "培训班-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trainingClassService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "培训班-通过id查询")
    @ApiOperation(value = "培训班-通过id查询", notes = "培训班-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrainingClass trainingClass = trainingClassService.getById(id);
        return Result.OK(trainingClass);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trainingClass
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrainingClass trainingClass) {
        return super.exportXls(request, trainingClass, TrainingClass.class, "培训班");
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
        return super.importExcel(request, response, TrainingClass.class);
    }

}
