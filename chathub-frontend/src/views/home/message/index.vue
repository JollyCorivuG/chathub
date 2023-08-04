<template>
    <img :src="generateFileIcon('11.jpg')" alt="">
</template>

<script setup lang="ts">
// import {onMounted} from "vue";
// import {buildWSUrl} from "@/utils/websocket/build_url.ts";
// import {WS} from "@/utils/websocket";
// onMounted(() => {
//     const url = buildWSUrl(localStorage.getItem('token') as string, 1)
//     new WS(url)
//
// })
import {generateFileIcon} from "@/utils/file.ts";
</script>

<style scoped lang="scss">

</style>