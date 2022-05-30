package org.jeecg.modules.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * @author: WangMeng
 * @time: 2022-3-31
 * @description:
 */
public class ErrorMsgUtil {

    public static StringBuffer checkError(StringBuffer buffer, Object obj, String msg) {
        if (Objects.isNull(obj)) {
            buffer.append(msg);
        } else if (ObjectUtils.isEmpty(obj)) {
            buffer.append(msg);
        }
        return buffer;
    }

    public static StringBuffer checkError(StringBuffer buffer, String str, String msg) {
        if (StringUtils.isBlank(str)) {
            buffer.append(msg);
        }
        return buffer;
    }

    public static StringBuffer checkError(StringBuffer buffer, boolean b, String msg) {
        if (b) {
            buffer.append(msg);
        }
        return buffer;
    }
}
