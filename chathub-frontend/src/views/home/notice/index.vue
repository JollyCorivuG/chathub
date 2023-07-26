<template>
    <van-collapse v-model="active" accordion>
        <van-collapse-item title="好友通知" name="friend-notice" icon="contact">
            <van-swipe-cell v-if="friendNoticeList.length != 0" v-for="(friendNotice, index) in friendNoticeList" :key="index">
                <div class="notice-container">
                    <div class="notice-info">
                        <div class="left">
                            <van-image width="50px" height="50px" :src="friendNotice.showUserInfo.avatarUrl" round/>
                        </div>
                        <div class="right">
                            <div class="nickname" style="color: black;font-size: 18px">
                                <span>{{friendNotice.showUserInfo.nickName}}</span>
                            </div>
                            <div class="description">
                                <span>{{friendNotice.noticeType == 0 ? '请求添加对方为好友': friendNotice.description}}</span>
                            </div>
                            <div class="create-time">
                                <span>{{friendNotice.createTime}}</span>
                            </div>
                        </div>
                    </div>
                    <div class="status">
                        <div v-if="friendNotice.noticeType == 0">
                            <span v-if="friendNotice.statusInfo == 0" style="margin-right: 10px">等待验证</span>
                            <span v-else-if="friendNotice.statusInfo == 1" style="margin-right: 10px">已通过</span>
                            <span v-else style="margin-right: 10px">未通过</span>
                        </div>
                        <div v-else>
                            <span v-if="friendNotice.statusInfo == 3" style="margin-right: 10px">已拒绝</span>
                            <span v-else-if="friendNotice.statusInfo == 4" style="margin-right: 10px">已同意</span>
                            <div v-else style="display:flex;align-items: center">
                                <van-button style="border: none; padding: 0;color: #1989FA; margin-right: 8px;" @click="friendApplyReply(friendNotice, true)">同意</van-button>
                                <van-button style="border: none; padding: 0; color:#7f7f7f; margin-right: 10px" @click="friendApplyReply(friendNotice, false)">拒绝</van-button>
                            </div>
                        </div>
                    </div>
                </div>
                <template #right>
                    <van-button square text="删除" type="danger" style="height: 100%;" @click="deleteFriendNotice(friendNotice.id)"/>
                </template>
            </van-swipe-cell>
            <van-empty v-else description="暂无好友通知" />
        </van-collapse-item>
        <van-collapse-item title="群通知" name="group-notice" icon="cluster-o">
<!--            TODO 群通知-->
        </van-collapse-item>
    </van-collapse>
</template>

<script setup lang="ts">
import {ref, onMounted} from "vue";
import {FriendNotice, FriendNoticeList, FriendNoticeListResponse} from "@/api/notice/type.ts";
import {reqFriendNoticeList, reqDeleteFriendNotice} from "@/api/notice";
import {showNotify} from "vant";
import {convertJavaTimeToString} from "@/utils/time_format";
import {CommonResponse, HandleFriendRequestParams} from "@/api/friend/type.ts";
import {reqFriendApplyReply} from "@/api/friend";

let active = ref<string>('')

// 页面渲染时需要获取通知列表
const friendNoticeList = ref<FriendNoticeList>([]) // 好友通知列表
onMounted(async () => {
    const resp: FriendNoticeListResponse = await reqFriendNoticeList()
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
    }
    friendNoticeList.value = resp.data
    friendNoticeList.value.forEach((friendNotice) => {
        friendNotice.createTime = convertJavaTimeToString(friendNotice.createTime)
    })
    console.log(friendNoticeList.value)
})

// 同意或拒绝好友申请
const friendApplyReply = async (friendNotice: FriendNotice, isAccept: boolean) => {
    const params: HandleFriendRequestParams = {
        noticeId: friendNotice.id,
        userId: friendNotice.showUserInfo.id,
        isAccept: isAccept
    }
    console.log(params)
    const resp: CommonResponse = await reqFriendApplyReply(params)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    friendNotice.statusInfo = isAccept ? 4 : 3
}

// 删除好友通知
const deleteFriendNotice = async (friendNoticeId: number) => {
    const resp: CommonResponse = await reqDeleteFriendNotice(friendNoticeId)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    friendNoticeList.value = friendNoticeList.value.filter((friendNotice) => {
        return friendNotice.id != friendNoticeId
    })
}

</script>

<style scoped lang="scss">
.notice-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .notice-info {
        padding: 5px 0;
        display: flex;
        align-items: center;
        .right {
            display: flex;
            flex-direction: column;
            margin-left: 12px;
            font-size: 14px;
            color: #7f7f7f;
        }
    }
}

</style>