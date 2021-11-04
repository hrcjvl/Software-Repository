package com.bootdo.importdata.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Data
@TableName("time_to_failure")
public class ProjectItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键uuid
     */
    private Integer id;
    /**
     * 失效序号
     */
    private String failurenumber;
    /**
     * 失效时间间隔
     */
    private String timetofail;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFailure_number(String failure_number) {
        this.failurenumber = failure_number;
    }

    public void setTime_to_fail(String time_to_fail) {
        this.timetofail = time_to_fail;
    }

    public Integer getId() {
        return id;
    }

    public String getFailure_number() {
        return failurenumber;
    }

    public String getTime_to_fail() {
        return timetofail;
    }
}
