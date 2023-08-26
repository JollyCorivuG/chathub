<template>
    <div class="top_tab">
        <div class="left">
            <img :src="userStore.userInfo.avatarUrl" alt="user-avatar" @click="goInfoPage"/>
            <span>{{userStore.userInfo.nickName}}</span>
        </div>
        <div class="right">
            <van-popover :actions="actions" @select="onSelect" placement="bottom-end" theme="dark" duration="0.1">
                <template #reference>
                    <div class="operator" style="display: flex; align-items: center; height: 100%;">
                        <van-icon name="plus" size="1.8em" color="white"/>
                    </div>
                </template>
            </van-popover>
        </div>
    </div>
</template>

<script setup lang="ts">
import useUserStore from "@/pinia/modules/user"
import {useRoute, useRouter} from "vue-router";
import {showNotify} from "vant";

// 用户相关信息
const userStore = useUserStore()

// 弹出框
const router = useRouter()
const route = useRoute()
const actions = [
    { text: '创建群聊', icon: 'shield-o'},
    { text: '加好友/群', icon: 'contact'},
    { text: '发布说说', icon: 'share-o'},
    {text: '退出登录', icon: 'revoke'}
];
const onSelect = (item: any) => {
    switch (item.text) {
        case '创建群聊':
            router.push({
                path: '/create_group',
            })
            break
        case '加好友/群':
            router.push({
                path: '/add/friend',
                query: {
                    from: route.path
                }
            })
            break
        case '发布说说':
            router.push({
                path: '/create_talk',
            })
            break
        case '退出登录':
            doLogout()
            break
    }
}

// 退出登录, 跳转到登录页面时, 将账号显示在输入框中
const doLogout = async () => {
    await userStore.logout()
    await router.push({
        path: '/login',
        query: {
            account: userStore.userInfo.account
        }
    })
    showNotify({type: 'primary', message: '登出成功!'})
}

// 去到个人主页
const goInfoPage = () => {
    router.push({
        path: '/info'
    })
}
</script>

<style scoped lang="scss">
.top_tab {
    width: 100%;
    position: fixed;
    z-index: 999;
    // 实现#00D2FC到#27B7FC的渐变色
    background: linear-gradient(90deg, #00D2FC 0%, #27B7FC 100%);
    height: 8%;
    padding: 0 15px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .left {
        height: 100%;
        padding: 8px 0;
        display: flex;
        align-items: center;
        img {
            height: 100%;
            width: auto;
            border-radius: 50%;
        }
        span {
            margin-left: 10px;
            font-size: 20px;
            color: white;
            font-weight: bold;
        }
    }
    .right {
        height: 100%;
        display: flex;
        align-items: center;
        ::v-deep(.van-popover__wrapper) {
            height: 100%;
            display: flex;
            align-items: center;
        }
    }
}
</style>