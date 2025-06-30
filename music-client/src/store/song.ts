
import { Icon } from "@/enums";

export default {
  state: {
    /** 音乐信息 */

    videoUrl: "",//视频URL

  },





  getters: {

    videoUrl: (state) => state.videoUrl,



  },
  mutations: {

    setVideoUrl: (state, videoUrl) => {
      state.videoUrl = videoUrl;
    },

  },
  actions: {
    playMusic: ({ commit }, { id, url, pic, index, songTitle, singerName, lyric, currentSongList, videoUrl }) => {
      commit("setSongId", id);
      commit("setSongUrl", url);
      commit("setSongPic", pic);
      commit("setCurrentPlayIndex", index);
      commit("setSongTitle", songTitle);
      commit("setSingerName", singerName);
      commit("setLyric", lyric);
      commit("setCurrentPlayList", currentSongList);
      commit("setVideoUrl", videoUrl);
    },
  },
};