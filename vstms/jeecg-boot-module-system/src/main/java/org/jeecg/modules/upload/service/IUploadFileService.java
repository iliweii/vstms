package org.jeecg.modules.upload.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.upload.entity.UploadFile;
import org.jeecg.modules.upload.vo.UploadFileVO;

import java.util.List;

/**
 * @Description: 附件
 * @Author: jeecg-boot
 * @Date: 2021-06-02
 * @Version: V1.0
 */
public interface IUploadFileService extends IService<UploadFile> {

    IPage<UploadFileVO> page(Page<UploadFileVO> page, QueryWrapper<UploadFileVO> queryWrapper, UploadFile uploadFile);

    /**
     * 在业务保存的时候对附件和业务主键进行绑定
     *
     * @param ids      附件id列表
     * @param objectId 业务主键
     */
    void updateByIds(List<String> ids, String objectId);

    /**
     * 根据合同id查询合同相关附件列表
     *
     * @param page         分页参数
     * @param queryWrapper 查询构造器
     * @param file         附件
     * @return 附件分页列表
     */
    IPage<UploadFile> pageByContractId(Page<UploadFile> page, QueryWrapper<UploadFile> queryWrapper, UploadFile file);

    /**
     * 复制文件到某个业务类型<br/>
     * 其中，file中存放被复制的附件id，businessType中存放新的业务类型
     *
     * @param uploadFile 附件VO
     */
    void copy(UploadFileVO uploadFile);

}
