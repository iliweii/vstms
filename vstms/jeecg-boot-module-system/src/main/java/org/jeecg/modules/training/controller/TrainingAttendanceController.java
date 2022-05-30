package org.jeecg.modules.training.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.training.entity.TrainingAttendance;
import org.jeecg.modules.training.service.ITrainingAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Description: 考勤管理
 * @Author: jeecg-boot
 * @Date: 2022-05-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "考勤管理")
@RestController
@RequestMapping("/training/trainingAttendance")
public class TrainingAttendanceController extends JeecgController<TrainingAttendance, ITrainingAttendanceService> {
    @Autowired
    private ITrainingAttendanceService trainingAttendanceService;

    /**
     * 分页列表查询
     *
     * @param trainingAttendance
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "考勤管理-分页列表查询")
    @ApiOperation(value = "考勤管理-分页列表查询", notes = "考勤管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrainingAttendance trainingAttendance,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrainingAttendance> queryWrapper = QueryGenerator.initQueryWrapper(trainingAttendance, req.getParameterMap());
        Page<TrainingAttendance> page = new Page<TrainingAttendance>(pageNo, pageSize);
        IPage<TrainingAttendance> pageList = trainingAttendanceService.page(page, queryWrapper);
        Result<IPage<TrainingAttendance>> result = Result.OK(pageList);

        result.setMessage(this.trainingAttendanceService.queryTodayAd(trainingAttendance.getClassNo(), trainingAttendance.getAtdDate()));

        return result;
    }

    /**
     * 添加
     *
     * @param trainingAttendance
     * @return
     */
    @AutoLog(value = "考勤管理-添加")
    @ApiOperation(value = "考勤管理-添加", notes = "考勤管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrainingAttendance trainingAttendance) {
        trainingAttendanceService.save(trainingAttendance);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trainingAttendance
     * @return
     */
    @AutoLog(value = "考勤管理-编辑")
    @ApiOperation(value = "考勤管理-编辑", notes = "考勤管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrainingAttendance trainingAttendance) {
        trainingAttendanceService.updateById(trainingAttendance);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "考勤管理-通过id删除")
    @ApiOperation(value = "考勤管理-通过id删除", notes = "考勤管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trainingAttendanceService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "考勤管理-批量删除")
    @ApiOperation(value = "考勤管理-批量删除", notes = "考勤管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trainingAttendanceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "考勤管理-通过id查询")
    @ApiOperation(value = "考勤管理-通过id查询", notes = "考勤管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrainingAttendance trainingAttendance = trainingAttendanceService.getById(id);
        return Result.OK(trainingAttendance);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trainingAttendance
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrainingAttendance trainingAttendance) {
        return super.exportXls(request, trainingAttendance, TrainingAttendance.class, "考勤管理");
    }

    /**
     * 下载导入模板
     */
    @RequestMapping(value = "/exportTemplate")
    public ModelAndView exportTemplate(@RequestParam(name = "classNo", required = true) String classNo) {
        return this.trainingAttendanceService.exportTemplate(classNo);
    }

    /**
     * 下载错误信息
     */
    @RequestMapping(value = "/errorXls")
    public ModelAndView errorXls() {
        return this.trainingAttendanceService.errorXls();
    }

    /**
     * 通过excel导入数据
     *
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件流对象", required = true, dataType = "__File")})
    public Result<?> importExcel(@RequestParam(value = "file") MultipartFile multipartFile) {
        if (StringUtils.isEmpty(multipartFile.getOriginalFilename()))
            return Result.error("文件不存在");
        return this.trainingAttendanceService.importExcel(multipartFile);
    }

}
