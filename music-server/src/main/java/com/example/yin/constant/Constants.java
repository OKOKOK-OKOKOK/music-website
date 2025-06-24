package com.example.yin.constant;

public class Constants {
    /* 歌曲图片，歌手图片，歌曲文件，歌单图片等文件的存放路径 */
    public static String ASSETS_PATH = System.getProperty("user.dir");
    
    public static String AVATOR_IMAGES_PATH = "file:" + ASSETS_PATH + "/img/avatorImages/";
    public static String SONGLIST_PIC_PATH = "file:" + ASSETS_PATH + "/img/songListPic/";
    public static String SONG_PIC_PATH = "file:" + ASSETS_PATH + "/img/songPic/";
    public static String SONG_PATH = "file:" + ASSETS_PATH + "/song/";
    public static String SINGER_PIC_PATH = "file:" + ASSETS_PATH + "/img/singerPic/";
    public static String BANNER_PIC_PATH = "file:" + ASSETS_PATH + "/img/swiper/";

    /* MV相关存储路径 */
    public static String MV_PATH = "file:" + ASSETS_PATH + "/mv/";
    public static String MV_HD_PATH = "file:" + ASSETS_PATH + "/mv/hd/";     // 高清版本
    public static String MV_SD_PATH = "file:" + ASSETS_PATH + "/mv/sd/";     // 标清版本
    public static String MV_PREVIEW_PATH = "file:" + ASSETS_PATH + "/mv/preview/"; // 预览图
    public static String MV_COVER_PATH = "file:" + ASSETS_PATH + "/img/mvCover/";  // 封面图

    /* 盐值加密 */
    public static String SALT = "zyt";

}
