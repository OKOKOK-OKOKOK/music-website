package com.example.yin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.yin.common.R;
import com.example.yin.model.domain.Mv;
import com.example.yin.model.request.MvRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MvService extends IService<Mv> {
    //上传mv
    R addMv(MvRequest addMvRequest, MultipartFile mvFile);

    //更新mv数据
    R updateMvMsg(MvRequest updateMvRequest);

    //更新mv链接
    R updateMvUrl(MultipartFile mvFile, int id);

    //todo 上传视频封面,
    R addMvPic(MultipartFile picFile, int id);
    //更新mv封面
    R updateMvPic(MultipartFile picFile, int id);


    //删除mv
    R deleteMv(Integer id);

    //查询所有mv
    R allMv();

    //根据歌手id查询mv
    R mvOfSingerId(Integer singerId);

    //根据mv id查询mv
    R mvOfId(Integer id);

    //根据歌手名字查询mv
    R mvOfSingerName(String name);

    //todo更新视频预览?
    R updateMvPreview(MultipartFile previewFile, int id);

    // MV特有功能
    R getMvUrl(Integer id, String resolution);

    //生成mv预览
    R generatePreview(Integer mvId);

    //获取数据库中mv信息
    R getMvInfo(Integer songId);

    //根据歌曲id查询mv
    R mvOfSongId(int songId);
}