<template>
    <van-nav-bar
        title="账号信息"
        left-arrow
        @click-left="onClickLeft"
    />
    <van-cell title="头像" is-link value="内容" center @click="changeInfo">
        <template #value>
            <van-image
                round
                width="60px"
                height="60px"
                :src="userStore.userInfo.avatarUrl"
            />
        </template>
    </van-cell>
    <van-cell title="昵称" is-link :value="userStore.userInfo.nickName" @click="changeInfo" />
    <van-cell title="账号密码" is-link value="修改" @click="changeInfo" />
    <van-cell title="手机号" is-link @click="changeInfo">
        <template #value>
            {{ userStore.userInfo.phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2') }}
        </template>
    </van-cell>
    <van-cell title="状态">
        <template #value>
            <van-tag type="success" size="medium" v-if="userStore.userInfo.isOnline">在线</van-tag>
            <van-tag type="danger" size="medium" v-else>离线</van-tag>
        </template>
    </van-cell>
    <van-cell>
        <template #title>
            <van-button plain type="danger" block hairline @click="doLogout">退出登录</van-button>
        </template>
    </van-cell>

</template>

<script setup lang="ts">

// 点击左侧按钮, 返回上一页
import useUserStore from "@/pinia/modules/user";
import {showNotify} from "vant";
import {useRouter} from "vue-router";
const router = useRouter();
const onClickLeft = () => history.back();

// 个人信息
const userStore = useUserStore();

// 退出登录
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

// 修改信息
const changeInfo = () => {
    router.push({
        path: '/change_info',
    })
}
</script>

<style scoped lang="scss">

</style>