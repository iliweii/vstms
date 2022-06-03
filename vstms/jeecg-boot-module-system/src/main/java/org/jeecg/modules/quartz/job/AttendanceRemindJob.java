package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.training.entity.TrainingClass;
import org.jeecg.modules.training.entity.TrainingClassTeacher;
import org.jeecg.modules.training.service.ITrainingAttendanceService;
import org.jeecg.modules.training.service.ITrainingClassService;
import org.jeecg.modules.training.service.ITrainingClassTeacherService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 考勤提醒任务
 *
 * @Author Scott
 */
@Slf4j
public class AttendanceRemindJob implements Job {

    @Autowired
    private ITrainingClassService classService;
    @Autowired
    private ITrainingAttendanceService attendanceService;
    @Autowired
    private ITrainingClassTeacherService classTeacherService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    /**
     * 每天13点半执行一次，17点半执行一个，判断班主任是否上传了今日的考勤。
     * 若未上传，发送消息提醒。
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // 获取所有可用培训班（状态为录入中1 完成2 的）
        List<TrainingClass> classList = classService.lambdaQuery()
                .in(TrainingClass::getStatus, Arrays.stream((new String[]{"1", "2"})))
                .list();

        // 判断培训班今日考勤状态，若为考勤，给班主任发送提醒
        classList.forEach(clz -> {
            String s = attendanceService.queryTodayAd(clz.getNo(), new Date());
            if (StringUtils.equals("false", s)) {
                // 未上传，发送提醒
                // 先获取班主任
                TrainingClassTeacher headTeacher = classTeacherService.lambdaQuery()
                        .eq(TrainingClassTeacher::getClassNo, clz.getNo())
                        .eq(TrainingClassTeacher::getIsHead, "1")
                        .one();
                if (ObjectUtils.isNotEmpty(headTeacher)) {
                    sysBaseAPI.sendSysAnnouncement(new MessageDTO("admin", headTeacher.getUsername(),
                            "今日考勤未上传通知", "尊敬的班主任您好，" + clz.getName() +
                            "的今日考勤表还未上传，请及时下载考勤表模板并上传考勤情况。"));
                }
            }
        });

        log.info(String.format("考勤提醒定时任务，时间:" + DateUtils.getTimestamp()));

    }
}
