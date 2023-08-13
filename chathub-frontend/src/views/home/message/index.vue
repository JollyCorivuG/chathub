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
                            [文件] {{room.latestMsg.message.body.fileMsg?.fileName}}
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
                <van-button square text="删除" type="danger" style="height: 100%" @click="deleteRoom(room.id)"/>
            </template>
        </van-swipe-cell>
    </van-pull-refresh>
</template>

<script setup lang="ts">
// 获取会话列表
import {onBeforeUnmount, onMounted, ref} from "vue";
import type {CommonResponse, Room, RoomList, RoomListResponse} from "@/api/message/type.ts";
import {reqDeleteRoom, reqRoomList} from "@/api/message";
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

// 删除会话
const deleteRoom = async (roomId: number) => {
    const resp: CommonResponse = await reqDeleteRoom(roomId)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    roomList.value = roomList.value.filter(room => room.id != roomId)
}

// 当页面渲染完毕后，建立sse连接
onMounted(() => {
    // 1.利用EventSource对象，建立与服务器的连接, 我需要让请求头带上token并支持跨域
    const token: string = localStorage.getItem('token') as string || ''
    const eventSource = new EventSource(import.meta.env.VITE_API_PREFIX + '/sse/subscribe?' + 'token=' + token, {
        withCredentials: true
    })
    // 2.监听服务器端的消息
    eventSource.onmessage = (event) => {
        roomList.value = JSON.parse(event.data)
        getUnreadMsgCount()
    }

    // 当页面销毁时
    onBeforeUnmount(() => {
        // clearInterval(polling)
        // 关闭sse连接, 向服务器发送一个关闭连接的请求, /sse/close
        fetch(import.meta.env.VITE_API_PREFIX + '/sse/close', {
            method: 'GET',
            headers: {
                'token': localStorage.getItem('token') || ''
            }
        })
        eventSource.close()
    })

})
</script>

<style scoped lang="scss">
.container {
    height: calc(100% - 50px - 8% - 54px);
    overflow: auto;
}
.room {
    // 被点击时的背景色立刻变成灰色
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