package org.jeecg.modules.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.training.entity.TrainingAttendance;
import org.jeecg.modules.training.entity.TrainingClassStudent;
import org.jeecg.modules.training.excel.AttendanceSheetExport;
import org.jeecg.modules.training.excel.AttendanceSheetImport;
import org.jeecg.modules.training.mapper.TrainingAttendanceMapper;
import org.jeecg.modules.training.service.ITrainingAttendanceService;
import org.jeecg.modules.training.service.ITrainingClassStudentService;
import org.jeecg.modules.utils.ErrorMsgUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: 考勤管理
 * @Author: jeecg-boot
 * @Date: 2022-05-25
 * @Version: V1.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TrainingAttendanceServiceImpl extends ServiceImpl<TrainingAttendanceMapper, TrainingAttendance> implements ITrainingAttendanceService {

    private final ITrainingClassStudentService trainingClassStudentService;
    private final ISysUserService sysUserService;
    private final RedisUtil redisUtil;

    @Override
    public String queryTodayAd(String classNo, Date adDate) {
        // 查询今日考勤表是否上传 以及今日考勤情况
        int todayTotal = 0, todayAtd = 0;
        if (ObjectUtils.isNotEmpty(adDate)) {
            todayTotal = this.lambdaQuery()
                    .eq(TrainingAttendance::getClassNo, classNo)
                    .eq(TrainingAttendance::getAtdDate, adDate)
                    .count();
            todayAtd = this.lambdaQuery()
                    .eq(TrainingAttendance::getClassNo, classNo)
                    .eq(TrainingAttendance::getAtdDate, adDate)
                    .eq(TrainingAttendance::getAtdStatus, "1")
                    .count();
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String dateStr = formatter.format(date);
            todayTotal = this.lambdaQuery()
                    .eq(TrainingAttendance::getClassNo, classNo)
                    .eq(TrainingAttendance::getAtdDate, dateStr)
                    .count();
            todayAtd = this.lambdaQuery()
                    .eq(TrainingAttendance::getClassNo, classNo)
                    .eq(TrainingAttendance::getAtdDate, dateStr)
                    .eq(TrainingAttendance::getAtdStatus, "0")
                    .count();
        }

//        int total = this.trainingClassStudentService.lambdaQuery()
//                .eq(TrainingClassStudent::getClassNo, classNo)
//                .eq(TrainingClassStudent::getStatus, "1")
//                .count();

        if (todayTotal == 0) {
            // 说明今日考勤表未上传
            return "false";
        } else {
            // 已上传 | 总人数(在读) | 出勤人数
            return "true|" + todayTotal + "|" + todayAtd;
        }
    }

    @Override
    public ModelAndView exportTemplate(String classNo) {
        // Step.1 组装查询条件
        // Step.2 获取导出数据
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        String dateStr = formatter.format(date);
        String dateStr2 = formatter2.format(date);

        List<AttendanceSheetExport> pageList = this.baseMapper.getAttendanceSheet(classNo, dateStr);
        // 过滤选中数据
        // 组装结果


        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "考勤表"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, AttendanceSheetExport.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(dateStr2 + "考勤表", dateStr2 + "考勤表上传模板，用于培训班编号：" + classNo + "当日的考勤记录上传。考勤状态请填写：出勤、迟到、旷课、请假", "考勤表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    @Override
    public ModelAndView errorXls() {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<AttendanceSheetImport> errorList = (List<AttendanceSheetImport>) redisUtil.get("sys:errImport:attendance_sheet_" + user.getUsername());
        if (errorList == null) {
            throw new JeecgBootException("暂无错误信息");
        }
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "考勤表导入报错");
        mv.addObject(NormalExcelConstants.CLASS, AttendanceSheetImport.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("考勤表导入错误信息", "考勤表导入错误信息，请根据错误信息修改无误后，重新导入本文件", "考勤表导入错误信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, errorList);
        return mv;
    }

    @Override
    public Result<?> importExcel(MultipartFile multipartFile) {
        ImportParams params = new ImportParams();
        params.setTitleRows(2);
        params.setHeadRows(1);
        try {
            // Step.1 读取导入数据
            LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            List<AttendanceSheetImport> list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), AttendanceSheetImport.class, params);
            long start = System.currentTimeMillis();

            // Step.2 对数据进行初步校验
            List<AttendanceSheetImport> rightList = list;
            List<AttendanceSheetImport> errorList = new LinkedList<>();
            rightList = checkData(rightList, errorList);

            // Step.3 将导入数据转换为新增数据
            List<TrainingAttendance> importList = new LinkedList<>();
            rightList.forEach(detailImport -> {
                TrainingAttendance setupSpareDetail = new TrainingAttendance();
                BeanUtils.copyProperties(detailImport, setupSpareDetail);
                importList.add(setupSpareDetail);
            });

            // Step.4 新增数据入库
            importList.forEach(ad -> {
                TrainingAttendance adQuery = this.lambdaQuery()
                        .eq(TrainingAttendance::getClassNo, ad.getClassNo())
                        .eq(TrainingAttendance::getUsername, ad.getUsername())
                        .eq(TrainingAttendance::getAtdDate, ad.getAtdDate())
                        .one();
                if (ObjectUtils.isNotEmpty(adQuery)) this.baseMapper.updateById(ad);
                else this.baseMapper.insert(ad);
            });

            // Step.5 若有错误信息，存入缓存
            if (!CollectionUtils.isEmpty(errorList)) {
                redisUtil.set("sys:errImport:attendance_sheet_" + user.getUsername(), errorList, 10 * 60L);
            }

            // Step.6 返回导入结果
            log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
            return Result.importReturn(errorList.size(), rightList.size(), "请下载查看详情（10分钟内有效）", "/training/trainingAttendance/errorXls");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("文件导入失败:" + e.getMessage());
        } finally {
            try {
                multipartFile.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对数据进行校验
     *
     * @param rightList 数据列表
     * @param errorList 错误数据列表
     * @return 正确数据列表
     */
    private List<AttendanceSheetImport> checkData(List<AttendanceSheetImport> rightList, List<AttendanceSheetImport> errorList) {
        List<AttendanceSheetImport> tempList = new LinkedList<>();
        int len = rightList.size();
        for (AttendanceSheetImport detailImport : rightList) {
            StringBuffer stringBuffer = new StringBuffer();

            ErrorMsgUtil.checkError(stringBuffer, detailImport.getClassNo(), "培训班编号不能为空；");
            ErrorMsgUtil.checkError(stringBuffer, detailImport.getUsername(), "学员姓名不能为空；");
            ErrorMsgUtil.checkError(stringBuffer, detailImport.getAtdDate(), "考勤日期不能为空；");
            ErrorMsgUtil.checkError(stringBuffer, this.sysUserService.getUserByName(detailImport.getUsername()), "未找到学员" + detailImport.getUsername() + "；");
            ErrorMsgUtil.checkError(stringBuffer, detailImport.getAtdStatus(), "考勤状态不能为空；");
            if (!Arrays.asList(new String[]{"0", "1", "2", "3"}).contains(detailImport.getAtdStatus())) {
                ErrorMsgUtil.checkError(stringBuffer, true, "考勤状态填写有误，请填写如下状态：出勤、迟到、旷课、请假；");
            }
            if (!StringUtils.isEmpty(stringBuffer.toString())) {
                detailImport.setMsg(stringBuffer.toString());
                errorList.add(detailImport);
            } else {
                tempList.add(detailImport);
            }
        }
        return tempList;
    }
}
