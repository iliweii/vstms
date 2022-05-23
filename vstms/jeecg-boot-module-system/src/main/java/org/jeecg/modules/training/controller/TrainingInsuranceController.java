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
import org.jeecg.modules.training.entity.TrainingInsurance;
import org.jeecg.modules.training.service.ITrainingInsuranceService;
import org.jeecg.modules.training.vo.TrainingInsuranceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 保险办理
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "保险办理")
@RestController
@RequestMapping("/training/trainingInsurance")
public class TrainingInsuranceController extends JeecgController<TrainingInsurance, ITrainingInsuranceService> {
    @Autowired
    private ITrainingInsuranceService trainingInsuranceService;

    /**
     * 分页列表查询
     *
     * @param trainingInsurance
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "保险办理-分页列表查询")
    @ApiOperation(value = "保险办理-分页列表查询", notes = "保险办理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrainingInsuranceVO trainingInsurance,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrainingInsuranceVO> queryWrapper = QueryGenerator.initQueryWrapper(trainingInsurance, req.getParameterMap());
        Page<TrainingInsuranceVO> page = new Page<TrainingInsuranceVO>(pageNo, pageSize);
        IPage<TrainingInsuranceVO> pageList = trainingInsuranceService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 编辑
     *
     * @param trainingInsurance
     * @return
     */
    @AutoLog(value = "保险办理-编辑")
    @ApiOperation(value = "保险办理-编辑", notes = "保险办理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrainingInsurance trainingInsurance) {
        trainingInsuranceService.edit(trainingInsurance);
        return Result.OK("编辑成功!");
    }

}
