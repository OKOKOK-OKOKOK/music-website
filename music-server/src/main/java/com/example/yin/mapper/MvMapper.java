package com.example.yin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.yin.model.domain.Mv;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MvMapper extends BaseMapper<Mv> {

}


//
//@Repository
//public interface MvMapper extends BaseMapper<Mv> {
//
//    /**
//     * 获取MV总播放量
//     * @param mvId MV ID
//     * @return 播放量总数
//     */
//    int selectPlayCountSum(@Param("mv_id") Long mvId);
//
//    /**
//     * 查询用户是否收藏过该MV
//     * @param consumerId 用户ID
//     * @param mvId MV ID
//     * @return 收藏记录ID，未收藏返回null
//     */
//    Integer selectUserCollect(@Param("consumer_id") Long consumerId, @Param("mv_id") Long mvId);
//
//    /**
//     * 根据歌曲ID查询关联的MV
//     * @param songId 歌曲ID
//     * @return MV列表
//     */
//    List<Mv> selectBySongId(@Param("song_id") Long songId);
//
//    /**
//     * 更新MV播放量
//     * @param mvId MV ID
//     * @param increment 增量值
//     * @return 影响的行数
//     */
//    int updatePlayCount(@Param("mv_id") Long mvId, @Param("increment") int increment);
//}