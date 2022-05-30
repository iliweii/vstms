package org.jeecg.modules.training.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.training.entity.TrainingAttendance;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * @Description: 考勤管理
 * @Author: jeecg-boot
 * @Date: 2022-05-25
 * @Version: V1.0
 */
public interface ITrainingAttendanceService extends IService<TrainingAttendance> {

    /**
     * 查询某天的某班考勤情况
     *
     * @param classNo
     * @param adDate
     * @return
     */
    String queryTodayAd(String classNo, Date adDate);

    /**
     * 下载导入模板
     *
     * @return
     */
    ModelAndView exportTemplate(String classNo);

    /**
     * 下载错误信息
     *
     * @return
     */
    ModelAndView errorXls();

    /**
     * 导入
     *
     * @param multipartFile
     * @return
     */
    Result<?> importExcel(MultipartFile multipartFile);

}
