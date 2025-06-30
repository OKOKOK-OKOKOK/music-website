package com.example.yin.controller;

import com.example.yin.common.R;
import com.example.yin.model.request.MvRequest;
import com.example.yin.service.MvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MvController {

    @Autowired
    private MvService mvService;

    //----------mv增加与删除
     //添加MV
    @PostMapping("/mv/add")
    public R addMv(MvRequest addMvRequest, @RequestParam("file") MultipartFile mvFile) {
        return mvService.addMv(addMvRequest, mvFile);
    }

    // 删除MV
    @DeleteMapping("/mv/delete")
    public R deleteMv(@RequestParam int id) {
        return mvService.deleteMv(id);
    }

    //----------------------------------获取mv
    // 获取所有MV
    @GetMapping("/mv")
    public R allMv() {
        return mvService.allMv();
    }

    //根据歌歌手名字获取对应的mv
    @GetMapping("/mv/singer/singername")
    public R mvOfSingerName(@RequestParam String name) {
        return mvService.mvOfSingerName(name);
    }

    //根据歌曲名字获取mv
    @GetMapping("/mv/song/songname")
    public R mvOfSongName(@RequestParam String name) {
        return mvService.mvOfSongName(name);
    }

    // 根据歌曲ID获取MV
    @GetMapping("/mv/song/songid")
    public R mvOfSongId(@RequestParam int songId) {
        return mvService.mvOfSongId(songId);
    }

    // 根据歌手ID获取MV
    @GetMapping("/mv/singer/singerid")
    public R mvOfSingerId(@RequestParam int singerId) {
        return mvService.mvOfSingerId(singerId);
    }

    // 获取MV播放地址
    @GetMapping("/mv/url")
    public R getMvUrl(@RequestParam int id, @RequestParam(required = false) String resolution) {
        return mvService.getMvUrl(id, resolution);
    }
    //----------------------------------更新mv
    // 更新MV信息
    @PutMapping("/mv/update")
    public R updateMvMsg(@RequestBody MvRequest updateMvRequest) {
        return mvService.updateMvMsg(updateMvRequest);
    }

    // 更新MV文件
    @PostMapping("/mv/url/update")
    public R updateMvUrl(@RequestParam("file") MultipartFile mvFile, @RequestParam("id") int id) {
        return mvService.updateMvUrl(mvFile, id);
    }

    // 更新MV封面
    @PostMapping("/mv/img/update")
    public R updateMvPic(@RequestParam("file") MultipartFile picFile, @RequestParam("id") int id) {
        return mvService.updateMvPic(picFile, id);
    }


}