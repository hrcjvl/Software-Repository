package com.bootdo.importdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bootdo.importdata.entity.ProjectItem;
import com.bootdo.importdata.entity.Result;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Mapper
public interface ExcelService extends IService<ProjectItem> {

    Result importProject(MultipartFile file);
}
