<template>
    <div class="top">
        <van-icon name="arrow-left" size="1.8em" color="#fff" @click="goBack"></van-icon>
        <div class="middle">
            {{friendInfo.nickName}}
        </div>
        <van-icon name="ellipsis" size="1.8em" color="#fff"></van-icon>
    </div>
    <div class="offset">
    </div>
    <div class="loading" v-show="isLoading">
        <van-button loading type="primary" loading-type="spinner" style="background-color: #F0F0F0; border: #F0F0F0; color: black"/>
    </div>
    <div class="content" ref="messagePanel" @mousewheel="loadMoreMsg">
        <div v-for="(msg, index) in msgStore.msgList" :key="index" :id="'message' + index">
            <div v-if="index != 0 && !isSameMinute(msg.message.sendTime, msgStore.msgList[index - 1].message.sendTime)" class="time">
                {{formatTime(msg.message.sendTime)}}
            </div>
            <div v-if="msg.fromUser.id == userStore.userInfo.id" class="right">
                <div>
                    <div v-if="msg.message.msgType == MsgType.TEXT" class="message">
                        {{msg.message.body.content}}
                    </div>
                    <div v-else-if="msg.message.msgType == MsgType.IMG">
                        <img :src="msg.message.body.imgMsg.url" alt="img-msg" :style="{height: msg.message.body.imgMsg.height + 'px', width: msg.message.body.imgMsg.width + 'px'}">
                    </div>
                    <div v-else-if="msg.message.msgType == MsgType.FILE">

                    </div>
                </div>
                <img :src="userStore.userInfo.avatarUrl" alt="user-avatar" class="user-avatar">
            </div>
            <div v-else class="left">
                <img :src="userStore.userInfo.avatarUrl" alt="user-avatar" class="user-avatar">
                <div>
                    <div v-if="msg.message.msgType == MsgType.TEXT" class="message">
                        {{msg.message.body.content}}
                    </div>
                    <div v-else-if="msg.message.msgType == MsgType.IMG">
                        <img :src="msg.message.body.imgMsg.url" alt="img-msg" :style="{height: msg.message.body.imgMsg.height + 'px', width: msg.message.body.imgMsg.width + 'px'}">
                    </div>
                    <div v-else-if="msg.message.msgType == MsgType.FILE">

                    </div>
                </div>
            </div>
        </div>
    </div>

    <div style="height: 42px;background-color: #F0F0F0;"></div>
    <div class="bottom">
        <van-field autosize type="textarea" :rows="1" v-model="content" @keyup.enter="sendMsg" />
        <div class="other-operator">
            <van-popover v-model:show="showEmojiPopover" placement="top" :show-arrow="false">
                <ul class="emoji-list">
                    <li v-for="emoji in emojiList" :key="emoji" @click="addEmoji(emoji)">
                        {{emoji}}
                    </li>
                </ul>
                <template #reference>
                    <van-icon name="smile-o" size="1.8em" style="margin-right: 2px;"></van-icon>
                </template>
            </van-popover>
            <van-uploader :max-size="mxImgSize" @oversize="overMxImgSize" v-model="sendImgList" :after-read="sendImg" :preview-image="false">
                <van-icon name="photo-o" size="1.8em" style="margin-right: 3px;"></van-icon>
            </van-uploader>
<!--            TODO 上传文件-->
            <van-icon name="paid" size="1.8em"></van-icon>
        </div>
        <van-button type="primary" block style="height: 2.1em;width: 6em;" :disabled="content.length == 0" @click="sendMsg">发送</van-button>
    </div>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {nextTick, onMounted, ref, watch} from "vue";
import {GetUserInfoResponse, UserInfo} from "@/api/user/type.ts";
import {reqGetUserInfoById} from "@/api/user";
import {showNotify, UploaderFileListItem} from "vant";
import {getEmoji} from "@/utils/emoji.ts";
import {reqUploadFile} from "@/api/upload";
import {UploadFileResponse} from "@/api/upload/type.ts";
import {ImgMsg, MsgType, SendMsg, SendMsgResponse, TextMsg} from "@/api/message/type.ts";
import {reqSendMsg} from "@/api/message";
import useMsgStore from "@/pinia/modules/message";
import {WS} from "@/utils/websocket";
import {buildWSUrl} from "@/utils/websocket/build_url.ts";
import useUserStore from "@/pinia/modules/user";
import {formatTime, isSameMinute} from "@/utils/time_format.ts";

// 返回上一页
const router = useRouter()
const goBack = () => {
    router.push('/home/message')
}

// 发送表情包
const emojiList = getEmoji()
const showEmojiPopover = ref<boolean>(false)
const addEmoji = (emoji: string) => {
    content.value += emoji
}

// 发送图片
const sendImgList = ref<UploaderFileListItem[]>([] as UploaderFileListItem[])
const mxImgSize = ref<number>(1024 * 1024)
const overMxImgSize = () => {
    showNotify({type: 'danger', message: '图片大小不能超过1MB！'})
}
const sendImg = async () => {
    if (sendImgList.value.length == 0) {
        showNotify({type: 'danger', message: '请选择一张图片！'})
    }

    // 将图片长传到服务器
    const rawImg: UploaderFileListItem = sendImgList.value[0]
    const file: File = new File([rawImg.file as any], <string>rawImg.file?.name, {type: rawImg.file?.type})
    const formData = new FormData()
    formData.append('file', file)
    const resp: UploadFileResponse = await reqUploadFile(formData)
    if (resp.statusCode != 0) {
        sendImgList.value = []
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    sendImgList.value = []
    const img: HTMLImageElement = new Image();
    img.src = resp.data
    img.onload = async () => {
        const originWidth: number = img.width
        img.width = 200 > img.width ? img.width : 200
        img.height = img.height * (img.width / originWidth)
        img.width = Math.floor(img.width)
        img.height = Math.floor(img.height)
        const msgBody: ImgMsg = {
            size: rawImg.file?.size as number,
            width: img.width,
            height: img.height,
            url: resp.data
        }
        const msg: SendMsg = {
            roomId: msgStore.roomId,
            msgType: MsgType.IMG,
            body: msgBody
        }
        const resp1: SendMsgResponse = await reqSendMsg(msg)
        if (resp1.statusCode != 0) {
            showNotify({type: 'danger', message: resp1.statusMsg})
            return
        }
        msgStore.msgList.push(resp1.data)
        await nextTick(() => {
            if (messagePanel.value) {
                messagePanel.value.scrollTop = messagePanel.value.scrollHeight
            }
        })
    }
}

// 发送文本消息
const content = ref<string>('')
const sendMsg = async () => {
    content.value = content.value.replace(/\n/g, '')
    if (content.value.length == 0) {
        showNotify({type: 'danger', message: '消息不能为空！'})
        return
    }
    const msgBody: TextMsg = {
        content: content.value
    }
    content.value = ''
    const msg: SendMsg = {
        roomId: msgStore.roomId,
        msgType: MsgType.TEXT,
        body: msgBody
    }
    const resp: SendMsgResponse = await reqSendMsg(msg)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    msgStore.msgList.push(resp.data)
    await nextTick(() => {
        if (messagePanel.value) {
            messagePanel.value.scrollTop = messagePanel.value.scrollHeight
        }
    })
}

// 当页面渲染后, 拿到数据
const msgStore = useMsgStore()
const route = useRoute()
const friendInfo = ref<UserInfo>({} as UserInfo)
msgStore.roomId = Number(useRoute().query.roomId)

// 得到用户信息
const getFriendInfo = async () => {
    const userId: number = Number(route.query.friendId)
    const resp: GetUserInfoResponse = await reqGetUserInfoById(userId)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    friendInfo.value = resp.data
}
getFriendInfo()

// 得到消息列表
const getMsgList = async (): Promise<number> => {
    return await msgStore.getMsgList()
}

// 连接websocket
const userStore = useUserStore()
new WS(buildWSUrl(userStore.token, msgStore.roomId))

// 让页面渲染完成后, 滚动到底部
const messagePanel = ref<HTMLElement>()
onMounted(async () => {
    await getMsgList()
    if (messagePanel.value) {
        messagePanel.value.addEventListener('scroll', loadMoreMsg, true)
        messagePanel.value.scrollTop = messagePanel.value.scrollHeight
    }
})

// 当滚动条到达顶部时, 加载更多消息
const isLoading = ref<boolean>(false)
const loadMoreMsg = async () => {
    if (isLoading.value) {
        return
    }
    const scrollTop = messagePanel.value?.scrollTop
    if (scrollTop == 0) {
        isLoading.value = true
        const num: number = await getMsgList()
        await nextTick(() => {
            isLoading.value = false
            if (messagePanel.value) {
                let height = 0
                for (let i = 0; i < num; i++) {
                    const div = document.getElementById('message' + i)
                    if (div) {
                        height += div.clientHeight
                    }
                }
                messagePanel.value.scrollTop = height
            }
        })
    }
}

// 监视msgStore.isReceiveMsg的变化, 如果发生变化就滚动到底部
watch(() => msgStore.isReceiveMsg, async (newVal, oldVal) => {
    if (newVal != oldVal) {
        await nextTick(() => {
            if (messagePanel.value) {
                messagePanel.value.scrollTop = messagePanel.value.scrollHeight
            }
        })
    }
})

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

.content {
    padding-top: 18px;
    background-color: #F0F0F0;
    height: calc(100vh - 8% - 42px);
    overflow-y: scroll;
}

.bottom {
    position: fixed;
    bottom: 0;
    padding: 5px;
    background-color: #F8F8F8;
    width: 100%;
    display: flex;
    align-items: center;
    .other-operator {
        padding: 0 3px;
        display: flex;
        align-items: center;
        margin-top: 3px;
    }
    ::v-deep(.van-cell) {
        padding: 1px 0;
        height: 26px;
    }
}
.emoji-list {
    display: flex;
    flex-wrap: wrap;
    padding: 5px;
    li {
        width: 10%;
        height: 25px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
    }
}

.content {
    .user-avatar {
        margin: 0 7px;
        width: 38px;
        height: 38px;
        border-radius: 50%;
    }
    .left {
        padding-bottom: 18px;
        display: flex;
        align-items: flex-start;
        .message {
            // 圆角上下左右
            border-radius: 8px 8px 8px 8px;
            max-width: 240px;
            padding: 10px;
            background-color: white;
            color: black;
            word-break: break-all;
            display: flex;
            align-items: center;
        }
    }
    .right {
        padding-bottom: 18px;
        display: flex;
        justify-content: flex-end;
        align-items: flex-start;
        .message {
            display: flex;
            align-items: center;
            border-radius: 8px 8px 8px 8px;
            max-width: 240px;
            padding: 10px;
            background-color: #0199FF;
            color: white;
            word-break: break-all;
        }
    }
    .time {
        padding-bottom: 18px;
        text-align: center;
        color: #8A8E99;
        font-size: 15px;
    }
}
.loading {
    position: fixed;
    z-index: 999;
    display: flex;
    justify-content: center;
    width: 100%;
}
</style>