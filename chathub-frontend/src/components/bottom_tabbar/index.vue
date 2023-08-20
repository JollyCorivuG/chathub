<template>
    <van-tabbar v-model="selected" @change="onChange">
        <van-tabbar-item name="message" icon="chat-o" v-if="roomStore.unReadMsgCount > 0" :badge="roomStore.unReadMsgCount">消息</van-tabbar-item>
        <van-tabbar-item name="message" icon="chat-o" v-else>消息</van-tabbar-item>
        <van-tabbar-item name="contact" icon="friends-o">联系人</van-tabbar-item>
        <van-tabbar-item name="notice" icon="bullhorn-o">通知</van-tabbar-item>
        <van-tabbar-item name="trend" icon="star-o">动态</van-tabbar-item>
    </van-tabbar>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {useRouter, useRoute} from "vue-router";
import useRoomStore from "@/pinia/modules/room";

// 未读消息数量
const roomStore = useRoomStore()

// tab拦切换
const router = useRouter()
const route = useRoute()
const selected = ref<string>(route.path.split('/').pop() as string)
const onChange = (name: string) => {
    selected.value = name
    router.push('/home/' + name)
}
</script>

<style scoped lang="scss">

</style>