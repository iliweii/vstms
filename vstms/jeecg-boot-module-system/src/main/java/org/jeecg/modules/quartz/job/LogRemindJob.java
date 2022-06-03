package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.training.entity.TrainingClassStudent;
import org.jeecg.modules.training.entity.TrainingClassTeacher;
import org.jeecg.modules.training.entity.TrainingLog;
import org.jeecg.modules.training.service.ITrainingAttendanceService;
import org.jeecg.modules.training.service.ITrainingClassStudentService;
import org.jeecg.modules.training.service.ITrainingClassTeacherService;
import org.jeecg.modules.training.service.ITrainingLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 日志提醒任务
 *
 * @Author Scott
 */
@Slf4j
public class LogRemindJob implements Job {

    @Autowired
    private ITrainingLogService logService;
    @Autowired
    private ITrainingAttendanceService attendanceService;
    @Autowired
    private ITrainingClassStudentService classStudentService;
    @Autowired
    private ITrainingClassTeacherService classTeacherService;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    /**
     * 每天17点执行一个，判断用户是否上传了今日的日志。
     * 若未上传，发送消息提醒。
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // 获取所有的学生（在读）、教师、班主任、督导
        List<TrainingClassStudent> students = classStudentService.lambdaQuery()
                .eq(TrainingClassStudent::getStatus, "1").list();
        List<TrainingClassTeacher> teachers = classTeacherService.list();
        List<String> usernameList = students.stream().map(TrainingClassStudent::getUsername).collect(Collectors.toList());
        usernameList.addAll(teachers.stream().map(TrainingClassTeacher::getUsername).collect(Collectors.toList()));

        // 今日已考勤名单
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<TrainingLog> todayLog = logService.lambdaQuery()
                .likeRight(TrainingLog::getCreateTime, sdf.format(date))
                .list();
        List<String> usernameToday = todayLog.stream().map(TrainingLog::getCreateBy).collect(Collectors.toList());

        // 生成排除后的名单（未提交日志的）
        usernameList = usernameList.stream().filter(e -> !usernameToday.contains(e)).collect(Collectors.toList());

        // 给未提交日志的人发送消息提醒
        sysBaseAPI.sendSysAnnouncement(new MessageDTO("admin", String.join(",", usernameList),
                "今日日志未上传提醒", "请及时上传今日日志。"));

        log.info(String.format("日志提醒定时任务，时间:" + DateUtils.getTimestamp()));

    }
}
