package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.common.R;
import com.example.yin.model.domain.Mv;
import com.example.yin.model.request.MvRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MvService extends IService<Mv> {

    R addMv(MvRequest addMvRequest, MultipartFile mvFile);

    R updateMvMsg(MvRequest updateMvRequest);

    R updateMvUrl(MultipartFile mvFile, int id);

    R updateMvPic(MultipartFile picFile, int id);

    R deleteMv(Integer id);

    R allMv();

    R mvOfSingerId(Integer singerId);

    R mvOfId(Integer id);

    R mvOfSingerName(String name);

    R updateMvPreview(MultipartFile previewFile, int id);

    // MV特有功能
    R getMvUrl(Integer id, String resolution);

    R generatePreview(Integer mvId);

    R getMvInfo(Integer songId);

    R mvOfSongId(int songId);
}