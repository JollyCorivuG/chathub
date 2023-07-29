<template>
    <van-empty image="error" image-size="300px">
        <template #description>
            <div style="font-size: 70px">404!</div>
        </template>
        <van-button round type="primary" @click="back" style="margin-top: 25px">返回上一页</van-button>
    </van-empty>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
const router = useRouter();
const back = () => {
    router.back();
};
</script>

<style scoped lang="scss">

</style>