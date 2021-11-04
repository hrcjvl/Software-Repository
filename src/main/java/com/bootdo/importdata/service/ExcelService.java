package com.bootdo.importdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bootdo.common.domain.DictDO;
import com.bootdo.importdata.entity.ProjectItem;
import com.bootdo.importdata.entity.Result;
import com.bootdo.importdata.utils.PageUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExcelService extends IService<ProjectItem> {

    Result importProject(MultipartFile file);
    List<ProjectItem> show(Map<String, Object> map);
    int count(Map<String, Object> map);
    ProjectItem get(Long id);
    int update(ProjectItem dict);

    int save1(ProjectItem dict);
}
