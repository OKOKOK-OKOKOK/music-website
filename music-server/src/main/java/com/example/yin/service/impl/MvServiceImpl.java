package com.example.yin.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.yin.common.R;
import com.example.yin.controller.MinioUploadController;
import com.example.yin.mapper.MvMapper;
import com.example.yin.mapper.SongMapper;
import com.example.yin.model.domain.Mv;
import com.example.yin.model.domain.Song;
import com.example.yin.model.request.MvRequest;
import com.example.yin.model.request.SongRequest;
import com.example.yin.service.MvService;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Service
public class MvServiceImpl extends ServiceImpl<MvMapper, Mv> implements MvService {
    @Autowired
    private MvMapper mvMapper;

    @Value("${minio.mv-bucket}")
    private String mvBucketName;

    @Autowired
    MinioClient minioClient;

    @Override
    public R addMv(MvRequest mvRequest, MultipartFile mvFile){
        Mv mv = new Mv();
        BeanUtils.copyProperties(mvRequest, mv);
        String pic = "/img/songPic/tubiao.jpg";
        String fileName = mvFile.getOriginalFilename();
        String s = MinioUploadController.uploadFile(mvFile);
        String storeUrlPath = "/"+mvBucketName+"/" + fileName;
        mv.setCreateTime(new Date());
        mv.setUpdateTime(new Date());
        mv.setStorageKey(storeUrlPath);

        if (s.equals("File uploaded successfully!")&&mvMapper.insert(mv) > 0) {
            return R.success("上传成功", storeUrlPath);
        } else {
            return R.error("上传失败");
        }
    }

//    @Override
//    public R addMv(MvRequest addMvRequest, MultipartFile mvFile) {
//
//        Mv mv = new Mv();
//        BeanUtils.copyProperties(addMvRequest, mv);
//        String pic= "/img/songPic/tubiao.jpg";//TODO: 要修改路径
//
//        return null;
//    }


//    @Override
//    public R addMv(MvRequest addMvRequest, MultipartFile mvFile) {
//        // 1. 参数校验
//        if (mvFile == null || mvFile.isEmpty()) {
//            return R.error("MV文件不能为空");
//        }
//
//        // 2. 创建MV实体并复制属性
//        Mv mv = new Mv();
//        BeanUtils.copyProperties(addMvRequest, mv);//?
//
//        // 3. 上传MV文件到MinIO
//        String fileName = mvFile.getOriginalFilename();
//        String uploadResult = MinioUploadController.uploadMvFile(mvFile);
//        String storeUrlPath = "/" + mvBucketName + "/" +addMvRequest.getResolution() + "/" + fileName;
//
//        // 4. 设置MV属性
//        mv.setStorageKey(storeUrlPath);
//        mv.setCreateTime(new Date());
//        mv.setUpdateTime(new Date());
//
//        // 设置默认封面（如果请求中未提供）
////        if (addMvRequest.getPicUrl() == null || addMvRequest.getPicUrl().isEmpty()) {
////            mv.setPicUrl("/img/mv/default.jpg");
////        }
//
//        // 5. 保存到数据库
//        if (uploadResult.equals("File uploaded successfully!") && mvMapper.insert(mv) > 0) {
//            return R.success("MV添加成功", new HashMap<String, Object>() {{
//                put("id", mv.getId());
//                put("url", storeUrlPath);
//            }});
//        } else {
//            // 上传失败时尝试删除已上传的文件
//            try {
//                minioClient.removeObject(
//                        RemoveObjectArgs.builder()
//                                .bucket(mvBucketName)
//                                .object(fileName)
//                                .build());
//            } catch (Exception e) {
//                log.error("删除上传失败的文件出错", e);
//            }
//            return R.error("MV添加失败");
//        }
//    }
//
//@Override
//public R addMv(MvRequest addMvRequest, MultipartFile mvFile) {
//
//
//    // 1. 参数校验
//    // 重点检查：确保 songId 存在
//    if (addMvRequest.getSongId() == null) {
//        return R.error("必须指定关联歌曲ID");
//    }
//    // 检查其他必填字段
//    if (StringUtils.isEmpty(addMvRequest.getTitle())) {
//        return R.error("MV标题不能为空");
//    }
//    if (mvFile == null || mvFile.isEmpty()) {
//        return R.error("MV文件不能为空");
//    }
//
//    // 2. 创建MV实体并复制属性
//    Mv mv = new Mv();
//    try {
//        BeanUtils.copyProperties(addMvRequest, mv);
//    } catch (Exception e) {
//        log.error("属性复制失败", e);
//        return R.error("请求参数转换失败: " + e.getMessage());
//    }
//
//    // 3. 生成安全的文件名和对象键
//    String originalFilename = mvFile.getOriginalFilename();
//    String safeFilename = originalFilename != null ?
//            originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_") :
//            "mv_" + System.currentTimeMillis();
//
//    String resolution = addMvRequest.getResolution() != null ?
//            addMvRequest.getResolution() : "HD";
//
//    // 创建MinIO对象键（与uploadMvFile方法匹配）
//    String objectKey = "mv/" + safeFilename;
//    String storeUrlPath = "/" + mvBucketName + "/" + resolution + "/" + safeFilename;
//
//    // 4. 上传MV文件到MinIO
//    String uploadResult = null;
//    try {
//        uploadResult = MinioUploadController.uploadMvFile(mvFile);
//
//        // 检查上传结果
//        if (!"File uploaded successfully!".equals(uploadResult)) {
//            log.error("MinIO上传失败: {}");
//            return R.error("MV文件上传失败: " + uploadResult);
//        }
//    } catch (Exception e) {
//        log.error("文件上传过程中发生异常", e);
//        return R.error("文件上传异常: " + e.getMessage());
//    }
//
//    // 5. 设置MV属性
//    try {
//        // 注意：这里存储的是完整路径（与上传路径一致）
//        mv.setStorageKey(storeUrlPath);
//        mv.setCreateTime(new Date());
//        mv.setUpdateTime(new Date());
//
//        // 设置其他必填字段
//        mv.setDuration(0); // 默认值，实际应解析视频
//        mv.setFileSize(mvFile.getSize());
//        mv.setResolution(resolution);
//
//    } catch (Exception e) {
//        log.error("设置MV属性失败", e);
//        // 属性设置失败也需要删除已上传的文件
//        deleteUploadedFile(objectKey);
//        return R.error("设置MV属性失败: " + e.getMessage());
//    }
//
//    // 6. 保存到数据库
//    try {
//        int insertResult = mvMapper.insert(mv);
//        if (insertResult <= 0) {
//            log.error("数据库插入失败，影响行数: {}");
//            deleteUploadedFile(objectKey);
//            return R.error("数据库保存失败");
//        }
//    } catch (DataIntegrityViolationException e) {
//        log.error("数据库约束违反", e);
//        deleteUploadedFile(objectKey);
//        return R.error("数据库保存失败: 字段约束错误 - " + e.getRootCause().getMessage());
//    } catch (Exception e) {
//        log.error("数据库操作异常", e);
//        deleteUploadedFile(objectKey);
//        return R.error("数据库保存失败: " + e.getMessage());
//    }
//
//    // 7. 返回成功响应
//    try {
//        return R.success("MV添加成功", new HashMap<String, Object>() {{
//            put("id", mv.getId());
//            put("url", storeUrlPath);
//        }});
//    } catch (Exception e) {
//        log.error("构建响应失败", e);
//        return R.error("操作成功但构建响应失败");
//    }
//}
//
//    // 辅助方法：删除已上传的文件（使用MinIO对象键）
//    private void deleteUploadedFile(String objectKey) {
//        if (objectKey == null) return;
//
//        try {
//            minioClient.removeObject(
//                    RemoveObjectArgs.builder()
//                            .bucket(mvBucketName)
//                            .object(objectKey) // 使用MinIO对象键
//                            .build());
//            log.error("已删除上传失败的文件: {}");
//        } catch (Exception e) {
//            log.error("删除文件失败: {}");
//        }
//    }
    @Override
    public R updateMvMsg(MvRequest updateMvRequest) {
        Mv mv = new Mv();
        BeanUtils.copyProperties(updateMvRequest, mv);
        mv.setUpdateTime(new Date());
        if (mvMapper.updateById(mv) > 0) {
            return R.success("MV信息更新成功");
        } else {
            return R.error("MV信息更新失败");
        }
    }

//    @Override
//    public R updateMvUrl(MultipartFile mvFile, int id) {
//        Mv mv =new Mv();
//        return null;
//    }

    @Override
    public R updateMvUrl(MultipartFile mvFile, int id) {
        // 1. Get existing MV record
        Mv mv = mvMapper.selectById(id);
        if (mv == null) {
            return R.error("MV不存在");
        }

        // 2. Extract filename from existing storage path
        String path = mv.getStorageKey();
        String[] parts = path.split("/");
        String oldFileName = parts[parts.length - 1];

        // 3. Delete old file from MinIO
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(mvBucketName)
                            .object(oldFileName)
                            .build());
        } catch (Exception e) {
            log.error("删除旧MV文件失败", e);
            return R.error("删除旧文件失败");
        }

        // 4. Upload new MV file
        String newFileName = mvFile.getOriginalFilename();
        String uploadResult = MinioUploadController.uploadMvFile(mvFile);
        String storeUrlPath = "/" + mvBucketName + "/" + newFileName;

        // 5. Update MV record
        mv.setStorageKey(storeUrlPath);
        mv.setUpdateTime(new Date());

        if (uploadResult.equals("File uploaded successfully!") && mvMapper.updateById(mv) > 0) {
            return R.success("MV文件更新成功", new HashMap<String, Object>() {{
                put("id", mv.getId());
                put("url", storeUrlPath);
            }});
        } else {
            // Rollback: Try to delete the newly uploaded file if update failed
            try {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(mvBucketName)
                                .object(newFileName)
                                .build());
            } catch (Exception e) {
                log.error("回滚删除新上传文件失败", e);
            }
            return R.error("MV文件更新失败");
        }
    }
//TUDO: 更改封面图片
    @Override
    public R updateMvPic(MultipartFile picFile, int id) {
        return null;
    }

    //删除mv
    @Override
    public R deleteMv(Integer id) {
        Mv mv = mvMapper.selectById(id);
        //TODO需要新加一个判断是否存在,通过判断path是否为空来判断是否存在；
        String path = mv.getStorageKey();
        String[] parts = path.split("/");
        String fileName = parts[parts.length - 1];
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(mvBucketName)
                            .object(fileName)
                            .build());
        } catch (Exception e) {
            log.error("删除MV文件失败", e);
            return R.error("删除文件失败");
        }
        if (mvMapper.deleteById(id) > 0) {
            return R.success("MV删除成功");
        } else {
            return R.error("MV删除失败");
        }
    }

    @Override
    public R allMv() {
        return R.success("success", mvMapper.selectList(null));
    }

    @Override
    public R mvOfSingerId(Integer singerId) {
        QueryWrapper<Mv> wrapper = new QueryWrapper<>();
        query().eq("singer_id", singerId);
        return R.success("null", mvMapper.selectList(wrapper));
    }

    @Override
    public R mvOfId(Integer id) {
        QueryWrapper<Mv> wrapper = new QueryWrapper<>();
        query().eq("id", id);
        return R.success("null", mvMapper.selectList(wrapper));
    }

    @Override
    public R mvOfSingerName(String name) {
        QueryWrapper<Mv> querywrapper = new QueryWrapper<>();
        querywrapper.like("singer_name",name);
        List<Mv> mvList = mvMapper.selectList(querywrapper);
        if(mvList.isEmpty()){
            return R.error("没有找到该歌手的MV");
        }
//        return R.success("null", mvMapper.selectList(querywrapper));
        return R.success("null", mvList);
    }

//TODO: 预览图，后面做
    @Override
    public R updateMvPreview(MultipartFile previewFile, int id) {
        return null;
    }

    @Override
    public R getMvUrl(Integer id, String resolution) {
        return null;
    }

    @Override
    public R generatePreview(Integer mvId) {
        return null;
    }

    //get  info by songId
    @Override
    public R getMvInfo(Integer songId) {
        QueryWrapper<Mv> wrapper = new QueryWrapper<>();
        wrapper.eq("song_id", songId)
                .orderByDesc("is_official")
                .last("LIMIT 1");

        Mv mv = mvMapper.selectOne(wrapper);

        if (mv == null) {
            return R.error("没有找到该歌曲的MV信息");
        }

        // Add additional info if needed
        HashMap<String, Object> result = new HashMap<>();
        result.put("mv", mv);

        return R.success("查询成功", result);
    }

    @Override
    public R mvOfSongId(int songId) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("song_id", songId);
        List<Mv> mvList = mvMapper.selectList(queryWrapper);
        if (mvList.isEmpty()) {
            return R.error("没有找到该歌曲的MV");
        }
        return R.success("null", mvList);
    }

}