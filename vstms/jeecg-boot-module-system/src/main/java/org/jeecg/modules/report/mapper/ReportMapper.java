package org.jeecg.modules.report.mapper;

import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface ReportMapper {

    @MapKey(value = "getAttednance")
    List<Map<String, Object>> getAttednance();

    @MapKey(value = "getStudyMaterial")
    List<Map<String, Object>> getStudyMaterial();

    @MapKey(value = "getLog")
    List<Map<String, Object>> getLog();

}
