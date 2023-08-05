<template>
    <van-search placeholder="请输入搜索关键词" />
    <van-pull-refresh v-model="loading" @refresh="onRefresh" class="container">
        <van-swipe-cell v-for="(room, index) in roomList" :key="index">
            <div class="room" @click="goChat(room)">
                <img :src="room.connectInfo.avatarUrl" alt="avatar" class="left">
                <div class="middle">
                    <div class="name">{{room.connectInfo.nickName}}</div>
                    <div class="message-content">
                        <span v-if="room.latestMsg.message.msgType == MsgType.TEXT">
                            {{room.latestMsg.message.body.content}}
                        </span>
                        <span v-else-if="room.latestMsg.message.msgType == MsgType.IMG">
                            [图片]
                        </span>
                        <span v-else-if="room.latestMsg.message.msgType == MsgType.FILE">
                            [文件] {{room.latestMsg.message.body.fileMsg.fileName}}
                        </span>
                    </div>
                </div>
                <div class="right">
                    <div class="time">
                        {{formatTime(room.latestMsg.message.sendTime)}}
                    </div>
                    <div class="unread-count" v-if="room.unreadCount != 0">
                        {{room.unreadCount}}
                    </div>
                </div>
            </div>
            <template #right>
                <van-button square text="删除" type="danger" style="height: 100%" />
            </template>
        </van-swipe-cell>
    </van-pull-refresh>
</template>

<script setup lang="ts">
// 获取会话列表
import {onMounted, ref} from "vue";
import type {Room, RoomList, RoomListResponse} from "@/api/message/type.ts";
import {reqRoomList} from "@/api/message";
import {showNotify} from "vant";
import {MsgType} from "@/api/message/type.ts";
import {formatTime} from "@/utils/time_format.ts";
import {useRouter} from "vue-router";
import useMsgStore from "@/pinia/modules/message";

// 会话列表
const roomList = ref<RoomList>([]);
const getRoomList = async () => {
    const resp: RoomListResponse = await reqRoomList()
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    roomList.value = resp.data
    getUnreadMsgCount()
}
getRoomList()

// 下拉刷新会话列表
const loading = ref<boolean>(false)
const onRefresh = async () => {
    await getRoomList()
    loading.value = false
    getUnreadMsgCount()
}

// 跳转到聊天页面
const router = useRouter()
const goChat = (room: Room) => {
    if (room.roomType == 0) {
        router.push({path: '/friend_chat', query: {roomId: room.id, friendId: room.connectInfo.id}})
    } else {
        // TODO 群聊
    }
}

// 得到未读消息数量
const msgStore = useMsgStore()
const getUnreadMsgCount = () => {
    msgStore.unReadMsgCount = 0
    for (let i = 0; i < roomList.value.length; i++) {
        msgStore.unReadMsgCount += roomList.value[i].unreadCount
    }
}

// 当页面渲染完毕后，开启定时器，每隔一段时间刷新一次会话列表
onMounted(() => {
    setInterval(() => {
        getRoomList()
    }, 2000)
})
</script>

<style scoped lang="scss">
.container {
    height: calc(100% - 50px - 8% - 54px);
    overflow: auto;
}
.room {
    // 被点击时的背景色为灰色
    &:active {
        background-color: #eee;
    }
    display: flex;
    .left {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        margin: 8px;
    }
    .middle {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;
        .name {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 5px;
        }
        .message-content {
            font-size: 14px;
            color: #999;
        }
    }
    .right {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: flex-end;
        margin: 10px;
        .time {
            font-size: 12px;
            color: #999;
        }
        .unread-count {
            margin-top: 5px;
            width: 16px;
            height: 16px;
            border-radius: 50%;
            background-color: #f00;
            color: #fff;
            font-size: 12px;
            padding-right: 1px;
            text-align: center;
            line-height: 16px;
        }
    }
}
</style>