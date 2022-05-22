package org.jeecg.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.boot.starter.lock.client.RedissonLockClient;
import org.jeecg.modules.system.entity.SysNo;
import org.jeecg.modules.system.mapper.SysNoMapper;
import org.jeecg.modules.system.service.ISysNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 流水号表ServiceImpl
 */
@Service
public class SysNoServiceImpl extends ServiceImpl<SysNoMapper, SysNo> implements ISysNoService {

    @Autowired
    private RedissonLockClient redissonLockClient;

    /**
     * 超时时间 5s
     */
    private static final int TIMEOUT = 5 * 1000;

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
    @Override
    public String generateNo(String format) {
        String originFormat = format;
        if (StringUtils.isEmpty(format))
            return null;
        // 位数默认为4
        int digit = 4;
        // 识别和处理"格式化字符串"中{}内的参数
        while (format.contains("{") && format.contains("}")) {
            String param = format.substring(format.indexOf("{") + 1, format.indexOf("}"));
            Date now = new Date();
            SimpleDateFormat ft;
            String date;
            switch (param) {
                case "Y":
                    ft = new SimpleDateFormat("yyyy");
                    date = ft.format(now);
                    format = replace(format, "{" + param + "}", date);
                    break;
                case "yM":
                    ft = new SimpleDateFormat("yyMM");
                    date = ft.format(now);
                    format = replace(format, "{" + param + "}", date);
                    break;
                case "YM":
                    ft = new SimpleDateFormat("yyyyMM");
                    date = ft.format(now);
                    format = replace(format, "{" + param + "}", date);
                    break;
                case "YMD":
                    ft = new SimpleDateFormat("yyyyMMdd");
                    date = ft.format(now);
                    format = replace(format, "{" + param + "}", date);
                    break;
//                case "CMP":
//                    LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//                    SysCompany sysCompany = sysCompanyService.lambdaQuery()
//                            .eq(SysCompany::getCmpNo, user.getSysCommunityCode()).one();
//                    String cmpShorter = sysCompany.getCmpShorter();
//                    format = replace(format, "{" + param + "}", cmpShorter);
//                    break;
                default:
                    Pattern pattern = Pattern.compile("[0-9]*");
                    if (pattern.matcher(param).matches()) {
                        digit = Integer.parseInt(param);
                    }
                    format = replace(format, "{" + param + "}", "");
                    break;
            }
        }

        if (format.contains("[") && format.contains("]")) {
            String substring = format.substring(format.indexOf("[") + 1, format.indexOf("]"));
            //此场景为传入业务归属主体
            format = replace(format, "[" + substring + "]", substring);
        }

        String prefix = format;
        // 上锁
        if (redissonLockClient.tryLock(prefix, -1, TIMEOUT)) {
            try {
                // 根据前缀查询流水号
                SysNo sysNo = this.lambdaQuery()
                        .eq(SysNo::getPrefix, prefix).one();
                if (ObjectUtil.isEmpty(sysNo)) {
                    sysNo = new SysNo();
                    sysNo.setPrefix(prefix);
                    sysNo.setNo(1);
                } else {
                    sysNo.setNo(sysNo.getNo() + 1);
                }
                sysNo.setFullNo(prefix + String.format("%0" + digit + "d", sysNo.getNo()));
                this.saveOrUpdate(sysNo);
                return sysNo.getFullNo();
            } finally {
                redissonLockClient.unlock(prefix);
            }
        }
        return generateNo(originFormat);
    }

    @Override
    public String generateYearMouthNo() {
        DateFormat format = new SimpleDateFormat("yyyyMM");
        String prefixDate = format.format(new Date());
        if (redissonLockClient.tryLock(prefixDate, -1, TIMEOUT)) {
            try {
                // 根据前缀查询流水号
                SysNo sysNo = this.lambdaQuery()
                        .eq(SysNo::getPrefix, prefixDate).one();
                if (ObjectUtil.isEmpty(sysNo)) {
                    sysNo = new SysNo();
                    sysNo.setPrefix(prefixDate);
                    sysNo.setNo(1);
                } else {
                    sysNo.setNo(sysNo.getNo() + 1);
                }
                sysNo.setFullNo(prefixDate + String.format("%03d", sysNo.getNo()));
                this.saveOrUpdate(sysNo);
                return sysNo.getFullNo();
            } finally {
                redissonLockClient.unlock(prefixDate);
            }
        }
        return null;
    }

    /**
     * 流水号生成功能中，将字符串中的参数名替换为参数值。
     *
     * @param a 父串
     * @param b 匹配子串
     * @param c 替换字串
     * @return
     */
    private String replace(String a, String b, String c) {
        StringBuilder sb = new StringBuilder(a);
        sb.replace(a.indexOf(b), a.indexOf(b) + b.length(), c);
        return sb.toString();
    }
}
