package org.jeecg.modules.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.training.entity.TrainingClass;
import org.jeecg.modules.training.entity.TrainingClassStudent;
import org.jeecg.modules.training.entity.TrainingClassTeacher;
import org.jeecg.modules.training.mapper.TrainingClassTeacherMapper;
import org.jeecg.modules.training.service.ITrainingClassService;
import org.jeecg.modules.training.service.ITrainingClassTeacherService;
import org.jeecg.modules.training.vo.TrainingLinkModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 培训班教师关系
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TrainingClassTeacherServiceImpl extends ServiceImpl<TrainingClassTeacherMapper, TrainingClassTeacher> implements ITrainingClassTeacherService {

    private final ISysBaseAPI sysBaseAPI;
    private final ITrainingClassService trainingClassService;
    private final JavaMailSender javaMailSender;
    private final ISysUserService sysUserService;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void edit(TrainingLinkModel trainingLinkModel) {
        // 批量新增关系
        List<TrainingClassTeacher> addList = new LinkedList<>();
        trainingLinkModel.getUsernameList().forEach(username -> {
            TrainingClassTeacher rel = new TrainingClassTeacher();
            rel.setClassNo(trainingLinkModel.getClassNo())
                    .setUsername(username)
                    .setIsHead("0");
            addList.add(rel);
        });
        this.saveBatch(addList);

        TrainingClass trainingClass = trainingClassService.getByNo(trainingLinkModel.getClassNo());
        // 批量发送消息通知 和 邮件通知
        List<String> usernames = addList.stream().map(TrainingClassTeacher::getUsername).collect(Collectors.toList());
        MessageDTO messageDTO = new MessageDTO("admin", String.join(",", usernames),
                "培训班入班通知", "尊敬的教师您好，您已被作为教师录入培训班：" + trainingClass.getName() +
                "（编号：" + trainingClass.getNo() + "），所学职业技能名称为" + trainingClass.getSkillName() +
                "，详情请登录系统查看。若您在教学过程中遇到任何问题都可以在系统的意见建议中反馈，或直接与督导反馈，" +
                "我们将努力解决您的问题。祝您工作愉快。");
        sysBaseAPI.sendSysAnnouncement(messageDTO);

        addList.forEach(e -> {
            SysUser user = sysUserService.getUserByName(e.getUsername());
            if (StringUtils.isNotEmpty(user.getEmail())) {
                sendMail(user.getEmail(), "【培训管理系统】培训班入班通知", messageDTO.getContent());
            }
        });
    }

    /**
     * 发送邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, true);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            log.error("发送邮件失败:" + e.getMessage());
        }
    }

}
