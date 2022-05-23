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
import org.jeecg.modules.training.entity.TrainingClassTeacher;
import org.jeecg.modules.training.service.ITrainingClassTeacherService;
import org.jeecg.modules.training.vo.TrainingLinkModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 培训班教师关系
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "培训班教师关系")
@RestController
@RequestMapping("/training/trainingClassTeacher")
public class TrainingClassTeacherController extends JeecgController<TrainingClassTeacher, ITrainingClassTeacherService> {
    @Autowired
    private ITrainingClassTeacherService trainingClassTeacherService;

    /**
     * 分页列表查询
     *
     * @param trainingClassTeacher
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "培训班教师关系-分页列表查询")
    @ApiOperation(value = "培训班教师关系-分页列表查询", notes = "培训班教师关系-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrainingClassTeacher trainingClassTeacher,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrainingClassTeacher> queryWrapper = QueryGenerator.initQueryWrapper(trainingClassTeacher, req.getParameterMap());
        Page<TrainingClassTeacher> page = new Page<TrainingClassTeacher>(pageNo, pageSize);
        IPage<TrainingClassTeacher> pageList = trainingClassTeacherService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 编辑
     *
     * @param trainingLinkModel
     * @return
     */
    @AutoLog(value = "培训班教师关系-编辑")
    @ApiOperation(value = "培训班教师关系-编辑", notes = "培训班教师关系-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrainingLinkModel trainingLinkModel) {
        trainingClassTeacherService.edit(trainingLinkModel);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "培训班教师关系-通过id删除")
    @ApiOperation(value = "培训班教师关系-通过id删除", notes = "培训班教师关系-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trainingClassTeacherService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "培训班教师关系-批量删除")
    @ApiOperation(value = "培训班教师关系-批量删除", notes = "培训班教师关系-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trainingClassTeacherService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

}
