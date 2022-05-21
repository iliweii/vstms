package org.jeecg.modules.upload.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecg.modules.upload.entity.UploadFile;
import org.jeecg.modules.upload.mapper.UploadFileMapper;
import org.jeecg.modules.upload.service.IUploadFileService;
import org.jeecg.modules.upload.vo.UploadFileVO;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 附件
 * @Author: jeecg-boot
 * @Date: 2021-06-02
 * @Version: V1.0
 */
@Service
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements IUploadFileService {

    @Override
    public IPage<UploadFileVO> page(Page<UploadFileVO> page, QueryWrapper<UploadFileVO> queryWrapper, UploadFile uploadFile) {
        return this.baseMapper.selectPageByCode(page, queryWrapper, uploadFile.getDictCode());
    }

    @Override
    public void updateByIds(List<String> ids, String objectId) {
        if (CollectionUtil.isNotEmpty(ids)) {
            UploadFile file = new UploadFile();
            file.setObjectId(objectId);
            for (String id : ids) {
                file.setId(id);
                this.updateById(file);
            }
        }
    }

    @Override
    public IPage<UploadFile> pageByContractId(Page<UploadFile> page, QueryWrapper<UploadFile> queryWrapper, UploadFile file) {
        return this.baseMapper.selectPageByContractId(page, queryWrapper, file.getDictCode(), file.getObjectId(), file.getFileTypeName());
    }

    @Override
    public void copy(UploadFileVO uploadFile) {
        List<UploadFile> files =
                this.lambdaQuery()
                        .in(UploadFile::getId, uploadFile.getFile().stream().map(JeecgEntity::getId).collect(Collectors.toList()))
                        .list();
        files.forEach(file -> {
            // 置空id，填充业务类型
            file.setId(null);
            file.setObjectId(uploadFile.getObjectId());
            file.setBusinessType(uploadFile.getBusinessType());
        });
        // 新增
        this.saveBatch(files);
    }
}
