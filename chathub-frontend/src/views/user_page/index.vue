<template>
    <div class="top">
        <van-icon name="arrow-left" size="1.8em" color="#fff" @click="goBack"></van-icon>
        <div class="middle">
            {{userInfo.nickName}}的主页
        </div>
        <van-icon name="info-o" size="1.8em" color="#fff"></van-icon>
    </div>
    <div class="offset"></div>
    <van-divider style="margin-top: 10px;margin-bottom: 5px">基本信息</van-divider>
    <div class="container">
        <div class="left">
            <img :src="userInfo.avatarUrl" alt="user-avatar"/>
            <div class="user-info">
                <div class="nickname">{{userInfo.nickName}}</div>
                <div class="account">账号: {{userInfo.account}}</div>
                <div class="phone">手机号: {{userInfo.phone}}</div>
            </div>
        </div>
        <div class="right">
            <van-icon name="good-job-o" size="1.3em" />
            <span>1234</span>
        </div>
    </div>

    <van-tabs v-model:active="active">
        <van-tab title="其他信息" name="other-info">
            <div class="other-info">
                <div class="up">
                    <van-circle
                        v-model:current-rate="fixed"
                        :rate="100"
                        :text="'好友数: ' + userInfo.friendCount"
                        size="125"
                    />
                    <van-circle
                        v-model:current-rate="fixed"
                        :rate="100"
                        :text="'群组数: ' + userInfo.groupCount"
                        color="#07C160"
                        size="125"
                    />
                </div>
                <div class="down">
                    <van-tag type="success" v-if="userInfo.isOnline" size="large">在线</van-tag>
                    <van-tag v-else size="large">离线</van-tag>
                </div>
                <van-notice-bar style="margin: 30px 10px;" scrollable left-icon="volume-o">
                    您与{{userInfo.nickName}}于{{userInfo.becomeFriendTime}}成为好友, 今天是你们成为好友的第{{becomeFriendDays}}天
                </van-notice-bar>
            </div>
        </van-tab>
        <van-tab title="共同好友" name="together-friend">共同好友</van-tab>
    </van-tabs>

    <div style="height: 50px;"></div>
    <van-action-bar>
        <van-action-bar-icon icon="good-job-o" text="点赞" />
        <van-action-bar-icon icon="share-o" text="分享" />
        <van-action-bar-button color="#EE0A24" text="删除好友" @click="deleteFriend"/>
        <van-action-bar-button type="primary" text="发送消息" @click="goChatPage"/>
    </van-action-bar>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import type {GetUserInfoResponse, UserInfo} from "@/api/user/type.ts";
import {reqGetUserInfoById} from "@/api/user";
import {showNotify} from "vant";
import {CommonResponse} from "@/api/friend/type.ts";
import {reqDeleteFriend} from "@/api/friend";

// 当页面渲染时, 拿到用户id发送
const route = useRoute()
const userInfo = ref<UserInfo>({} as UserInfo)
onMounted(async () => {
    const userId: number = Number(route.query.id)
    const resp: GetUserInfoResponse = await reqGetUserInfoById(userId)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    userInfo.value = resp.data
    // 将userInfo.value.becomeFriendTime以T拆分取前面一段
    userInfo.value.becomeFriendTime = userInfo.value.becomeFriendTime.split('T')[0]
    becomeFriendDays.value = Math.floor((new Date().getTime() - new Date(userInfo.value.becomeFriendTime).getTime()) / (1000 * 60 * 60 * 24)) + 1
})

// tab切换
const active = ref<string>('other-info')

// 其他信息, 包括好友数量、群组数量
const fixed = ref<number>(100)
const becomeFriendDays = ref<number>(0)
// TODO 共同好友

// 删除好友
const deleteFriend = async () => {
    const resp: CommonResponse = await reqDeleteFriend(userInfo.value.id)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    showNotify({type: 'success', message: '删除成功好友'})
    await router.push('/home/contact')
}

// 点击返回按钮
const router = useRouter()
const goBack = () => {
    router.push('/home/contact')
}

// 点击发送消息按钮
const goChatPage = () => {
    router.push({
        path: '/friend_chat',
        query: {
            friendId: userInfo.value.id,
            roomId: userInfo.value.roomId
        }
    })
}
</script>

<style scoped lang="scss">
.offset {
    height: 8%;
}
.top {
    width: 100%;
    position: fixed;
    z-index: 999;
    height: 8%;
    background: linear-gradient(90deg, #00D2FC 0%, #27B7FC 100%);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 15px;
    .middle {
        line-height: 20px;
        font-size: 20px;
        color: white;
        font-weight: bold;
    }
}
.container {
    padding: 0 15px 15px 15px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .left {
        display: flex;
        align-items: center;
        img {
            height: 75px;
            width: 75px;
            border-radius: 50%;
            border: 1px solid #eee;
        }
        .user-info {
            margin-left: 15px;
            height: 60px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            color: #7f7f7f;
            font-size: 14px;
            .nickname {
                font-size: 18px;
                font-weight: bold;
                color: black
            }
        }
    }
    .right {
        display: flex;
        flex-direction: column;
        align-items: center;
        span {
            font-size: 15px;
        }
    }
}
.other-info {
    padding: 15px;
    .up {
        display: flex;
        justify-content: space-evenly;
    }
    .down {
        margin-top: 30px;
        display: flex;
        width: 100%;
        justify-content: center;
    }
}
</style>