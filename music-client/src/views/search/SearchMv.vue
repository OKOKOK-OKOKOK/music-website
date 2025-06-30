<template>
  <div class="search-mv">
    <mv-list :mvList="currentMvList"></mv-list>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, watch, onMounted, getCurrentInstance } from "vue";
import { useStore } from "vuex";
import MvList from "@/components/MvList.vue"; // 假设您有一个MV列表组件
import { HttpManager } from "@/api";

export default defineComponent({
  components: {
    MvList,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();

    const currentMvList = ref([]); // 存放搜索到的MV
    const searchWord = computed(() => store.getters.searchWord);

    // 监听搜索词变化
    watch(searchWord, (value) => {
      searchMv(value);
    });

    // 搜索MV
    async function searchMv(value) {
      if (!value) {
        currentMvList.value = [];
        return;
      }

      try {
        // 尝试按歌曲名搜索
        const songResult = (await HttpManager.mvOfSongName(value)) as ResponseBody;

        if (songResult.data?.length) {
          currentMvList.value = songResult.data;
          return;
        }
        // 尝试按歌手名搜索
        const singerResult = (await HttpManager.mvOfSingerName(value)) as ResponseBody;

        if (singerResult.data?.length) {
          currentMvList.value = singerResult.data;
          return;
        }

        //尝试按照歌曲id搜索
        const songIdResult = (await HttpManager.getMvOfSongId(value)) as ResponseBody;

        if (songIdResult.data?.length) {
          currentMvList.value = songIdResult.data;
          return;
        }

        //尝试按照歌手id搜索
        const singerIdResult = (await HttpManager.getMvOfSingerId(value)) as ResponseBody;

        if (singerIdResult.data?.length) {
          currentMvList.value = singerIdResult.data;
          return;
        }

        // 如果都没找到
        currentMvList.value = [];
        (proxy as any).$message({
          message: "暂时没有相关MV",
          type: "warning",
        });
      } catch (error) {
        console.error("MV搜索失败:", error);
        (proxy as any).$message({
          message: "搜索失败，请稍后重试",
          type: "error",
        });
      }
    }

    // 初始化时根据路由参数搜索
    onMounted(() => {
      if (proxy.$route.query.keywords) {
        searchMv(proxy.$route.query.keywords as string);
      }
    });

    return {
      //向上暴露出存放mv数据的变量currentMvList
      currentMvList,
    };
  },
});
</script>