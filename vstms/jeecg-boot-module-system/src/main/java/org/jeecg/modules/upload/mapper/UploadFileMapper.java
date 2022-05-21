package org.jeecg.modules.upload.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.upload.entity.UploadFile;
import org.jeecg.modules.upload.vo.UploadFileVO;

/**
 * @Description: 附件
 * @Author: jeecg-boot
 * @Date: 2021-06-02
 * @Version: V1.0
 */
public interface UploadFileMapper extends BaseMapper<UploadFile> {

    IPage<UploadFileVO> selectPageByCode(Page<UploadFileVO> page, @Param(Constants.WRAPPER) QueryWrapper<UploadFileVO> queryWrapper, String code);

    /**
     * 根据合同id查询合同相关附件列表
     *
     * @param page         分页参数
     * @param queryWrapper 查询构造器
     * @param code         字典code
     * @param id           合同id
     * @param objId        归档id
     * @return 附件分页列表
     */
    IPage<UploadFile> selectPageByContractId(Page<UploadFile> page, @Param(Constants.WRAPPER) QueryWrapper<UploadFile> queryWrapper, String code, String id, String objId);

}
