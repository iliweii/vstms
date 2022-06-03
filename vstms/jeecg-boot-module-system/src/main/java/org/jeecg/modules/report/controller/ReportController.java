package org.jeecg.modules.report.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.report.mapper.ReportMapper;
import org.jeecg.modules.training.entity.TrainingAttendance;
import org.jeecg.modules.training.entity.TrainingLog;
import org.jeecg.modules.training.service.ITrainingAttendanceService;
import org.jeecg.modules.training.service.ITrainingLogService;
import org.jeecg.modules.upload.entity.UploadFile;
import org.jeecg.modules.upload.service.IUploadFileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Api(tags = "报表接口")
@RestController
@RequestMapping("/sys/report/")
@RequiredArgsConstructor
public class ReportController {

    private final ReportMapper reportMapper;
    private final ITrainingAttendanceService attendanceService;
    private final IUploadFileService uploadFileService;
    private final ITrainingLogService trainingLogService;

    private Date data = new Date();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @AutoLog(value = "出勤人数报表")
    @ApiOperation(value = "出勤人数报表", notes = "出勤人数报表")
    @GetMapping(value = "/cq")
    public JSONObject cq() {
        JSONObject json = new JSONObject();
        json.put("data", reportMapper.getAttednance());
        json.put("total", attendanceService.count());
        json.put("today", attendanceService.lambdaQuery().eq(TrainingAttendance::getAtdDate, sdf.format(data)).count());
        return json;
    }

    @AutoLog(value = "学习资料报表")
    @ApiOperation(value = "学习资料报表", notes = "学习资料报表")
    @GetMapping(value = "/xxzl")
    public JSONObject xxzl() {
        JSONObject json = new JSONObject();
        json.put("data", reportMapper.getStudyMaterial());
        json.put("total", uploadFileService.lambdaQuery()
                .eq(UploadFile::getBusinessType, "study_material").count());
        json.put("today", uploadFileService.lambdaQuery()
                .eq(UploadFile::getBusinessType, "study_material")
                .likeRight(UploadFile::getCreateTime, sdf.format(data)).count());
        return json;
    }

    @AutoLog(value = "日志数报表")
    @ApiOperation(value = "日志数报表", notes = "日志数报表")
    @GetMapping(value = "/rzs")
    public JSONObject rzs() {
        JSONObject json = new JSONObject();
        json.put("data", reportMapper.getLog());
        json.put("total", trainingLogService.count());
        json.put("today", trainingLogService.lambdaQuery()
                .likeRight(TrainingLog::getCreateTime, sdf.format(data)).count());
        return json;
    }

}
