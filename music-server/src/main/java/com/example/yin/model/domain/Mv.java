package com.example.yin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@Data
@TableName("mv")
public class Mv {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer songId;

    private String title;

    private String version;

    @TableField("resolution")
    private String resolution; // SD/HD/FHD/4K/8K

    private String storageKey;//存储路径

    private Integer duration; // 单位：秒

    private Long fileSize; // 单位：字节

    @TableField("is_official")
    private Boolean isofficial;

    private String director;

    private Date releaseDate;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    // 非数据库字段（用于关联查询）
    @TableField(exist = false)
    private String songName;

    @TableField(exist = false)
    private String singerName;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}