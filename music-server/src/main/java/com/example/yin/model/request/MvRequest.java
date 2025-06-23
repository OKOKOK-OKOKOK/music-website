package com.example.yin.model.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class MvRequest {

    private Integer id;//自增

    private Integer songId; // 关联歌曲ID

    private String title; // MV标题（可不同于歌曲名）

    private String version; // 版本：official(官方版)/live(现场版)/dance(舞蹈版)

    private String resolution; // 分辨率：SD/HD/FHD/4K

    private String storageKey; // 存储路径/对象存储Key

    private Integer duration; // 时长（秒）

    private Long fileSize; // 文件大小（字节）

    private Boolean isofficial; // 是否官方MV

    private String director; // 导演

    private Date releaseDate; // 发行日期

    private Date createTime;

    private Date updateTime;

    private Integer playCount; // 播放次数
}