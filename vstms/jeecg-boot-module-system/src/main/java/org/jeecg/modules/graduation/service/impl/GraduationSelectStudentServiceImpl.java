package org.jeecg.modules.graduation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jeecg.modules.graduation.entity.GraduationSelectStudent;
import org.jeecg.modules.graduation.mapper.GraduationSelectStudentMapper;
import org.jeecg.modules.graduation.service.IGraduationSelectStudentService;
import org.jeecg.modules.training.vo.TrainingLinkModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description: 评选优秀学员
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GraduationSelectStudentServiceImpl extends ServiceImpl<GraduationSelectStudentMapper, GraduationSelectStudent> implements IGraduationSelectStudentService {

    @Override
    public void link(TrainingLinkModel trainingLinkModel) {
        // 批量新增关系
        List<GraduationSelectStudent> addList = new LinkedList<>();
        trainingLinkModel.getUsernameList().forEach(username -> {
            GraduationSelectStudent rel = new GraduationSelectStudent();
            rel.setClassNo(trainingLinkModel.getClassNo())
                    .setStudent(username)
                    .setStatus("0");

            GraduationSelectStudent query = this.lambdaQuery()
                    .eq(GraduationSelectStudent::getClassNo, trainingLinkModel.getClassNo())
                    .eq(GraduationSelectStudent::getStudent, username)
                    .one();
            // 未查询到才添加，否则略过此条数据
            if (ObjectUtils.isEmpty(query))
                addList.add(rel);
        });
        this.saveBatch(addList);
    }

    @Override
    public void linkResult(TrainingLinkModel trainingLinkModel) {
        List<GraduationSelectStudent> editList = new LinkedList<>();
        // 批量编辑
        trainingLinkModel.getUsernameList().forEach(username -> {
            GraduationSelectStudent selectStudent = this.lambdaQuery()
                    .eq(GraduationSelectStudent::getClassNo, trainingLinkModel.getClassNo())
                    .eq(GraduationSelectStudent::getStudent, username)
                    .one();
            // 标记为优秀学员
            selectStudent.setStatus("1");
            editList.add(selectStudent);
        });

        this.updateBatchById(editList);
    }
}
