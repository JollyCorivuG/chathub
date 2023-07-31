<template>
    <van-cell is-link @click="goUserPage">
        <template #title>
            <div class="friend-info" :class="{'offline': !friend.isOnline}">
                <div class="left">
                    <van-image width="40px" height="40px" :src="friend.avatarUrl" round/>
                </div>
                <div class="right">
                    <div class="nickname" style="color: black;font-size: 16px;">
                        <span>{{friend.nickName}}</span>
                    </div>
                    <div class="tag">
                        <van-tag type="primary" style="margin-right: 8px;">好友</van-tag>
                        <van-tag type="success" v-if="friend.isOnline">在线</van-tag>
                        <van-tag v-else>离线</van-tag>
                    </div>
                </div>
            </div>
        </template>
        <template #right-icon>
        </template>
    </van-cell>
</template>

<script setup lang="ts">
import type {UserInfo} from "@/api/user/type.ts";
import {PropType} from "vue";
import {useRouter} from "vue-router";

// 接收父亲组件传递的数据['friend'], 类型是UserInfo
const props = defineProps({
    friend: {
        type: Object as PropType<UserInfo>,
        required: true
    }
})

// 点击单元格跳转到用户信息页
const router = useRouter()
const goUserPage = () => {
    router.push({
        path: '/user_page',
        query: {
            id: props.friend?.id
        }
    })
}
</script>

<style scoped lang="scss">
.van-cell {
    padding: 5px;
}
.friend-info {

    &.offline {
        // 如果是离线状态, 背景颜色变成灰色, 并透明
        opacity: 0.6;
    }
    display: flex;
    align-items: center;
    .left {
        display: flex;
        align-items: center;
    }
    .right {
        display: flex;
        flex-direction: column;
        margin-left: 5px;
        font-size: 14px;
        color: #7f7f7f;
    }
}
</style>