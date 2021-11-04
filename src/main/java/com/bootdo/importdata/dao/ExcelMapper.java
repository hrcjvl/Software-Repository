package com.bootdo.importdata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdo.common.domain.DictDO;
import com.bootdo.importdata.entity.ProjectItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.aspectj.asm.internal.ProgramElement;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: liyh
 * @Date: 2020/10/23 17:46
 */
@Mapper
public interface ExcelMapper extends BaseMapper<ProjectItem> {

    int insertProjectItem(@Param("Failure_number") String Failure_number, @Param("Time_to_fail") String Time_to_fail);
    List<ProjectItem> show(Map<String, Object> map);
    int count(Map<String, Object> map);
    ProjectItem get(Long id);
    int update(ProjectItem dict);
    int save1(ProjectItem dict);
}
