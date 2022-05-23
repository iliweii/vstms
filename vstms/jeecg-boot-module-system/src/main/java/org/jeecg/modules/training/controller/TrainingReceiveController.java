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
import org.jeecg.modules.training.entity.TrainingReceive;
import org.jeecg.modules.training.service.ITrainingReceiveService;
import org.jeecg.modules.training.vo.TrainingReceiveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 资料领取
 * @Author: jeecg-boot
 * @Date: 2022-05-24
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "资料领取")
@RestController
@RequestMapping("/training/trainingReceive")
public class TrainingReceiveController extends JeecgController<TrainingReceive, ITrainingReceiveService> {
    @Autowired
    private ITrainingReceiveService trainingReceiveService;

    /**
     * 分页列表查询
     *
     * @param trainingReceive
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "资料领取-分页列表查询")
    @ApiOperation(value = "资料领取-分页列表查询", notes = "资料领取-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrainingReceiveVO trainingReceive,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrainingReceiveVO> queryWrapper = QueryGenerator.initQueryWrapper(trainingReceive, req.getParameterMap());
        Page<TrainingReceiveVO> page = new Page<TrainingReceiveVO>(pageNo, pageSize);
        IPage<TrainingReceiveVO> pageList = trainingReceiveService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 编辑
     *
     * @param trainingReceive
     * @return
     */
    @AutoLog(value = "资料领取-编辑")
    @ApiOperation(value = "资料领取-编辑", notes = "资料领取-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrainingReceive trainingReceive) {
        trainingReceiveService.edit(trainingReceive);
        return Result.OK("编辑成功!");
    }

}
