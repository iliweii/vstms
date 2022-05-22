package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysNo;

/**
 * 流水号表Service
 */
public interface ISysNoService extends IService<SysNo> {

    /**
     * 生成流水号：参数参考SysNoParam枚举。<br/>
     * 格式化字符串：格式例如"JD-{YM}-{5}" 生成结果为"JD-202109-00251"(00251为流水号，其余为前缀)。<br/>
     * 再例如"GRN_{12}" 生成结果为"GRN_000000000002"(000000000002为流水号，其余为前缀)。<br/>
     * "{Y}{YM}{YMD}"表示日期，"{CMP}"表示当前用户所在公司公司简称。<br/>
     * "{5}"代表位数，5位，数字不足位数自动补0，数字位数非必写，默认为4位。<br/>
     * 流水号始终拼接在前缀之后。
     *
     * @param format 格式化字符串
     * @return 流水号
     */
    String generateNo(String format);


    String generateYearMouthNo();

}
