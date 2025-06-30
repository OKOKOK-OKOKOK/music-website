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
import com.example.yin.service.SongService;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
public class MvServiceImpl extends ServiceImpl<MvMapper, Mv> implements MvService {

    @Autowired
    private MvMapper mvMapper;

    @Value("${minio.mv-bucket}")
    private String mvBucketName;

    @Autowired
    MinioClient minioClient;

    @Autowired
    private SongService songService; // 用于校验歌曲是否存在

    // 允许的视频类型
    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
            "video/mp4", "video/avi", "video/mov", "video/wmv", "video/flv"
    );

    // 版本枚举值 (根据新数据库设计更新)
    private static final Set<String> VALID_VERSIONS = new HashSet<>(
            Arrays.asList("official", "live现场版", "dance舞蹈版", "二创", "OST", "其他")
    );

    // 分辨率枚举值
    private static final Set<String> VALID_RESOLUTIONS = new HashSet<>(
            Arrays.asList("SD", "HD", "FHD", "4K", "8K")
    );

    @Override
    public R addMv(MvRequest mvRequest, MultipartFile mvFile) {
        try {
            // 1. 基本参数空值校验
            if (mvRequest == null) {
                return R.error("请求参数不能为空");
            }
            if (mvFile == null || mvFile.isEmpty()) {
                return R.error("MV文件不能为空");
            }

//            // 2. 必填字段校验
//            if (mvRequest.getSongId() == null) {
//                return R.error("关联歌曲ID不能为空");
//            } else {
//                // 3. 关联歌曲存在性校验 - 使用正确的方法
//                if (!songService.existSong(mvRequest.getSongId())) {
//                    return R.error("关联歌曲不存在，ID: " + mvRequest.getSongId());
//                }
//            }
            // 4. 枚举值格式校验
            if (mvRequest.getVersion() != null && !VALID_VERSIONS.contains(mvRequest.getVersion())) {
                return R.error("无效的版本类型，允许值: " + String.join(",", VALID_VERSIONS));
            }
            if (mvRequest.getResolution() != null && !VALID_RESOLUTIONS.contains(mvRequest.getResolution())) {
                return R.error("无效的分辨率，允许值: " + String.join(",", VALID_RESOLUTIONS));
            }

            // 5. 数值范围校验
            if (mvRequest.getDuration() != null && mvRequest.getDuration() <= 0) {
                return R.error("时长必须大于0");
            }
            if (mvRequest.getFileSize() != null && mvRequest.getFileSize() <= 0) {
                return R.error("文件大小必须大于0");
            }

            // 6. 字符串长度校验
            if (mvRequest.getDirector() != null && mvRequest.getDirector().length() > 100) {
                return R.error("导演名称长度不能超过100字符");
            }

            // 7. 日期逻辑校验（发行日期不能晚于当前日期）
            if (mvRequest.getReleaseDate() != null && mvRequest.getReleaseDate().after(new Date())) {
                return R.error("发行日期不能晚于当前日期");
            }

            // 8. 文件类型校验
            String contentType = mvFile.getContentType();
            if (contentType == null || !ALLOWED_VIDEO_TYPES.contains(contentType)) {
                return R.error("不支持的文件类型，允许类型: " + String.join(",", ALLOWED_VIDEO_TYPES));
            }

            // 9. 文件大小校验（可选，如果前端已提供大小可对比）
            if (mvRequest.getFileSize() != null &&
                    mvRequest.getFileSize() != mvFile.getSize()) {
                return R.error("文件大小不一致");
            }
            // 所有校验通过，执行上传
            Mv mv = new Mv();
            BeanUtils.copyProperties(mvRequest, mv);
            String fileName = mvFile.getOriginalFilename();
            String s = MinioUploadController.uploadMvFile(mvFile);
            String storeUrlPath = "/" + mvBucketName + "/" + fileName;
            mv.setCreateTime(new Date());
            mv.setUpdateTime(new Date());
            mv.setStorageKey(storeUrlPath);

            // 设置文件实际大小（如果前端未提供）
            if (mv.getFileSize() == null) {
                mv.setFileSize(mvFile.getSize());
            }

            if (s.equals("File uploaded successfully!") && mvMapper.insert(mv) > 0) {
                return R.success("上传成功", storeUrlPath);
            } else {
                return R.error("上传失败");
            }
        } catch (Exception e) {
            log.error("MV上传失败", e);
            return R.error("系统错误: " + e.getMessage());
        }
    }

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
        if (mv == null) {
            return R.error("MV不存在或已被删除");
        }
        String path = mv.getStorageKey();
        if (path == null || path.equals("null")){
            return R.error("MV不存在");
        }
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
        querywrapper.like("singer_name", name);
        //但是mv表中没有这个字段,需要修改
        List<Mv> mvList = mvMapper.selectList(querywrapper);
        if (mvList.isEmpty()) {
            return R.error("没有找到该歌手的MV");
        }
//        return R.success("null", mvMapper.selectList(querywrapper));
        return R.success("null", mvList);
    }


    @Override
    public R mvOfSongName(String name) {
        QueryWrapper<Mv> querywrapper = new QueryWrapper<>();
        querywrapper.like("song_name", name);
        List<Mv> mvList = mvMapper.selectList(querywrapper);
        if (mvList.isEmpty()) {
            return R.error("没有找到该歌曲的MV");
        }
        return R.success("null", mvList);
    }

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

    @Override
    public R addMvPic(MultipartFile picFile, int id) {

        return null;
    }

}