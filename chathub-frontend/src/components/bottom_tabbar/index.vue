<template>
    <van-tabbar v-model="selected">
        <van-tabbar-item name="messages" icon="chat-o">消息</van-tabbar-item>
        <van-tabbar-item name="contacts" icon="friends-o">联系人</van-tabbar-item>
        <van-tabbar-item name="notices" icon="bullhorn-o">通知</van-tabbar-item>
        <van-tabbar-item name="trends" icon="star-o">动态</van-tabbar-item>
    </van-tabbar>
</template>

<script setup lang="ts">
import {ref} from "vue";

let selected = ref<string>('messages')
</script>

<style scoped lang="scss">

</style>