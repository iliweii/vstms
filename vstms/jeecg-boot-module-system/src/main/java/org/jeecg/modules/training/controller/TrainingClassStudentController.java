package org.jeecg.modules.training.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.training.entity.TrainingClassStudent;
import org.jeecg.modules.training.entity.TrainingClassTeacher;
import org.jeecg.modules.training.service.ITrainingClassStudentService;
import org.jeecg.modules.training.service.ITrainingClassTeacherService;
import org.jeecg.modules.training.vo.TrainingClassStudentVO;
import org.jeecg.modules.training.vo.TrainingLinkModel;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 培训班学生关系
 * @Author: jeecg-boot
 * @Date: 2022-05-23
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "培训班学生关系")
@RestController
@RequestMapping("/training/trainingClassStudent")
@RequiredArgsConstructor
public class TrainingClassStudentController extends JeecgController<TrainingClassStudent, ITrainingClassStudentService> {

    private final ITrainingClassStudentService trainingClassStudentService;
    private final ITrainingClassTeacherService trainingClassTeacherService;
    private final ISysUserService sysUserService;
    private final ISysUserRoleService sysUserRoleService;
    private final ISysDictService sysDictService;

    /**
     * 分页列表查询
     *
     * @param trainingClassStudent
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "培训班学生关系-分页列表查询")
    @ApiOperation(value = "培训班学生关系-分页列表查询", notes = "培训班学生关系-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrainingClassStudentVO trainingClassStudent,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TrainingClassStudentVO> queryWrapper = QueryGenerator.initQueryWrapper(trainingClassStudent, req.getParameterMap());
        Page<TrainingClassStudentVO> page = new Page<TrainingClassStudentVO>(pageNo, pageSize);
        IPage<TrainingClassStudentVO> pageList = trainingClassStudentService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 选择学生列表
     *
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "培训班学生关系-选择学生列表")
    @ApiOperation(value = "培训班学生关系-选择学生列表", notes = "培训班学生关系-选择学生列表")
    @GetMapping(value = "/userList")
    public Result<?> queryUserList(SysUser sysUser, String classNo, String type,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(sysUser, req.getParameterMap());
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);

        String roleId;
        List<String> usernameLists = new LinkedList<>();
        if ("student".equals(type)) {
            roleId = "1528588585151340546";
            List<TrainingClassStudent> students = trainingClassStudentService.lambdaQuery().eq(TrainingClassStudent::getClassNo, classNo).list();
            usernameLists = students.stream().map(TrainingClassStudent::getUsername).collect(Collectors.toList());
        } else {
            // teacher
            roleId = "1528588674632622082";
            List<TrainingClassTeacher> teachers = trainingClassTeacherService.lambdaQuery().eq(TrainingClassTeacher::getClassNo, classNo).list();
            usernameLists = teachers.stream().map(TrainingClassTeacher::getUsername).collect(Collectors.toList());
        }


        List<SysUserRole> roles = sysUserRoleService.lambdaQuery().eq(SysUserRole::getRoleId, roleId).list();
        List<String> roleIds = roles.stream().map(SysUserRole::getUserId).collect(Collectors.toList());

        if (usernameLists.size() > 0) queryWrapper.notIn("username", usernameLists);
        if (roleIds.size() > 0) queryWrapper.in("id", roleIds);
        else queryWrapper.eq("id", "-1");

        IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 编辑
     *
     * @param trainingClassStudent
     * @return
     */
    @AutoLog(value = "培训班学生关系-编辑")
    @ApiOperation(value = "培训班学生关系-编辑", notes = "培训班学生关系-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrainingClassStudent trainingClassStudent) {
        trainingClassStudentService.updateById(trainingClassStudent);
        return Result.OK("编辑成功!");
    }

    /**
     * 关联
     *
     * @param trainingLinkModel
     * @return
     */
    @AutoLog(value = "培训班学生关系-关联")
    @ApiOperation(value = "培训班学生关系-关联", notes = "培训班学生关系-关联")
    @PutMapping(value = "/link")
    public Result<?> link(@RequestBody TrainingLinkModel trainingLinkModel) {
        trainingClassStudentService.edit(trainingLinkModel);
        return Result.OK("关联成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "培训班学生关系-通过id删除")
    @ApiOperation(value = "培训班学生关系-通过id删除", notes = "培训班学生关系-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trainingClassStudentService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "培训班学生关系-批量删除")
    @ApiOperation(value = "培训班学生关系-批量删除", notes = "培训班学生关系-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trainingClassStudentService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 查询所属班级
     *
     * @return
     */
    @AutoLog(value = "培训班学生关系-查询所属班级")
    @ApiOperation(value = "培训班学生关系-查询所属班级", notes = "培训班学生关系-查询所属班级")
    @GetMapping(value = "/queryClass")
    public Result<?> queryClass(String username) {

        List<TrainingClassStudent> students = trainingClassStudentService.lambdaQuery()
                .eq(TrainingClassStudent::getUsername, username).list();
        List<TrainingClassTeacher> teachers = trainingClassTeacherService.lambdaQuery()
                .eq(TrainingClassTeacher::getUsername, username).list();
        List<String> studentClass = students.stream().map(TrainingClassStudent::getClassNo).collect(Collectors.toList());
        List<String> teacherClass = teachers.stream().map(TrainingClassTeacher::getClassNo).collect(Collectors.toList());
        studentClass.addAll(teacherClass);
        LinkedHashSet<String> classSet = new LinkedHashSet<>(studentClass);
        List<String> classNoList = new LinkedList<>(classSet);
        if (classNoList.size() > 0) {
            List<DictModel> dictModels = sysDictService.queryTableDictTextByKeys("training_class", "name", "no", classNoList);
            return Result.OK(dictModels.stream().map(DictModel::getText).collect(Collectors.joining(",")));
        } else {
            return Result.OK("暂不属于任何培训班");
        }
    }

}
