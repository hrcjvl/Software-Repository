package com.bootdo.importdata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bootdo.importdata.entity.ProjectItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/**
 * @Author: liyh
 * @Date: 2020/10/23 17:46
 */
@Mapper
public interface ExcelMapper extends BaseMapper<ProjectItem> {

    int insertProjectItem(@Param("orderNumber") String orderNumber, @Param("name") String name, @Param("content") String content,
                          @Param("type") Integer type, @Param("unit") String unit, @Param("price") String price, @Param("count") String count);
}
