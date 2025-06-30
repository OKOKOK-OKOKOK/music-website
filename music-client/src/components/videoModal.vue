
<template>
  <div class="video-modal" v-if="visible" @click.self="close">
    <div class="video-container">
      <video
          ref="videoPlayer"
          controls
          autoplay
          :src="videoUrl"
          @error="handleError"
      >
        您的浏览器不支持视频播放
      </video>
      <button class="close-btn" @click="close">×</button>
      <p v-if="errorMsg" class="error">{{ errorMsg }}</p>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch } from "vue";

export default defineComponent({
  props: {
    visible: Boolean,
    videoUrl: String
  },
  emits: ['close'],
  setup(props, { emit }) {
    const errorMsg = ref("");

    const close = () => {
      emit('close');
    };

    const handleError = (e: Event) => {
      errorMsg.value = "视频加载失败";
      console.error("Video error:", e);
    };

    watch(() => props.videoUrl, () => {
      errorMsg.value = ""; // 重置错误状态
    });

    return { close, handleError, errorMsg };
  }
});
</script>

<style scoped>
.video-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.video-container {
  position: relative;
  width: 80%;
  max-width: 800px;
}

video {
  width: 100%;
  outline: none;
}

.close-btn {
  position: absolute;
  top: -40px;
  right: 0;
  background: none;
  border: none;
  color: white;
  font-size: 2rem;
  cursor: pointer;
}

.error {
  color: red;
  text-align: center;
  margin-top: 10px;
}
</style>
}