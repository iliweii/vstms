package org.jeecg.modules.upload.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.upload.entity.UploadFile;
import org.jeecg.modules.upload.service.IUploadFileService;
import org.jeecg.modules.upload.vo.UploadFileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "附件管理")
@RestController
@RequestMapping("/sys/uploadFile")
public class UploadFileController extends JeecgController<UploadFile, IUploadFileService> {

    @Autowired
    private IUploadFileService uploadFileService;

    @Value(value = "${jeecg.path.upload}")
    private String uploadpath;

    @AutoLog(value = "附件管理-分页列表查询")
    @ApiOperation(value = "附件管理-分页列表查询", notes = "附件管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(UploadFileVO uploadFile,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (!StringUtils.isEmpty(uploadFile.getFileAlias())) {
            uploadFile.setFileAlias("," + uploadFile.getFileAlias() + ",");
        }
        if (!StringUtils.isEmpty(uploadFile.getFileName())) {
            uploadFile.setFileName("," + uploadFile.getFileName() + ",");
        }
        //uploadFile.setSysOrgCode(loginUser.getSysCommunityCode());
        QueryWrapper<UploadFileVO> queryWrapper = QueryGenerator.initQueryWrapper(uploadFile, req.getParameterMap());
        Page<UploadFileVO> page = new Page<>(pageNo, pageSize);
        IPage<UploadFileVO> pageList = uploadFileService.page(page, queryWrapper, uploadFile);

        List<UploadFileVO> voList = pageList.getRecords().stream().map(e -> {
            UploadFileVO vo = new UploadFileVO();
            BeanUtils.copyProperties(e, vo);
            File file = new File(uploadpath + e.getFilePath());
            vo.setFileSize(file.length());
            return vo;
        }).collect(Collectors.toList());
        pageList.setRecords(voList);

        return Result.OK(pageList);
    }

    @AutoLog(value = "附件管理-编辑，用于上传后点击保存，回调修改附件信息")
    // @ApiOperation(value = "附件管理-编辑", notes = "附件管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody UploadFile uploadFile) {
        UploadFile uploadFileQuery = uploadFileService.lambdaQuery()
                .eq(UploadFile::getFilePath, uploadFile.getFilePath()).one();
        if (ObjectUtil.isNull(uploadFileQuery)) {
            return Result.OK();
        }
        uploadFile.setId(uploadFileQuery.getId());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        uploadFile.setSysOrgCode(loginUser.getSysCommunityCode());
        uploadFileService.updateById(uploadFile);
        return Result.OK("编辑成功!");
    }

    /**
     * 附件管理-编辑，用于上传后点击保存，回调修改附件信息
     * （新）使用list回写，上面的接口已弃用
     */
    @PutMapping(value = "/editFile")
    public Result<?> editFile(@RequestBody UploadFileVO uploadFile) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        uploadFile.getFile().forEach(file -> {
            UploadFile uploadFileQuery = uploadFileService.lambdaQuery()
                    .eq(UploadFile::getFilePath, file.getFilePath()).one();
            file.setId(uploadFileQuery.getId());
            file.setSysOrgCode(loginUser.getSysCommunityCode());
            if (!StringUtils.isEmpty(uploadFile.getBusinessType())) file.setBusinessType(uploadFile.getBusinessType());
            if (!StringUtils.isEmpty(uploadFile.getFileType())) file.setFileType(uploadFile.getFileType());
            if (!StringUtils.isEmpty(uploadFile.getObjectId())) file.setObjectId(uploadFile.getObjectId());
        });
        uploadFileService.updateBatchById(uploadFile.getFile());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "附件管理-通过id删除")
    @ApiOperation(value = "附件管理-通过id删除", notes = "附件管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        this.uploadFileService.removeById(id);
        return Result.OK("删除成功!");
    }

    @AutoLog(value = "附件管理-通过id查询")
    @ApiOperation(value = "附件管理-通过id查询", notes = "附件管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        UploadFile uploadFile = uploadFileService.getById(id);
        return Result.OK(uploadFile);
    }


    @AutoLog(value = "附件管理-根据合同id查询合同相关附件列表")
    @ApiOperation(value = "附件管理-根据合同id查询合同相关附件列表", notes = "附件管理-根据合同id查询合同相关附件列表<br/>" +
            "附件包括：年度计划，招标申请，评委维护，招标结果，合同申请，合同签订，合同支付，合同主体审查，合同保函，合同退还，合同履约，补充协议的附件；")
    @GetMapping(value = "/listByContractId")
    public Result<?> listByContractId(UploadFile uploadFile,
                                      @RequestParam(name = "contId", defaultValue = "1") String contractId,
                                      @RequestParam(name = "objId", defaultValue = "1") String objId,
                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        uploadFile.setSysOrgCode(loginUser.getSysCommunityCode());
        QueryWrapper<UploadFile> queryWrapper = QueryGenerator.initQueryWrapper(uploadFile, req.getParameterMap());
        Page<UploadFile> page = new Page<UploadFile>(pageNo, pageSize);
        // 这里使用ObjectId临时存储合同id， 使用FileTypeName临时存储归档id
        uploadFile.setObjectId(contractId);
        uploadFile.setFileTypeName(objId);
        IPage<UploadFile> pageList = uploadFileService.pageByContractId(page, queryWrapper, uploadFile);
        return Result.OK(pageList);
    }

    @AutoLog(value = "附件管理-复制文件到某个业务类型")
    @ApiOperation(value = "附件管理-复制文件到某个业务类型", notes = "附件管理-复制文件到某个业务类型")
    @PostMapping(value = "/copy")
    public Result<?> copy(@RequestBody UploadFileVO uploadFile) {
        uploadFileService.copy(uploadFile);
        return Result.OK("操作成功!");
    }

}
