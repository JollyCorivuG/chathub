<template>
    <div class="top">
        <van-icon name="arrow-left" size="1.8em" color="#fff" @click="goBack"></van-icon>
        <div class="middle">
            {{friendInfo.nickName}}
        </div>
        <van-icon name="ellipsis" size="1.8em" color="#fff"></van-icon>
    </div>
    <div class="offset"></div>
    <div class="content">
        <div class="message-panel">
            <div v-for="(msg, index) in msgList" :key="index">
                {{msg.message}}
            </div>
            <van-button type="primary" @click="getMsgList">获取历史消息</van-button>
        </div>
    </div>

    <div style="height: 42px;background-color: #F0F0F0;"></div>
    <div class="bottom">
        <van-field autosize type="textarea" :rows="1" v-model="content"/>
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
import {useRouter, useRoute} from "vue-router";
import {ref} from "vue";
import {GetUserInfoResponse, UserInfo} from "@/api/user/type.ts";
import {reqGetUserInfoById} from "@/api/user";
import {showNotify, UploaderFileListItem} from "vant";
import {getEmoji} from "@/utils/emoji.ts";
import {reqUploadFile} from "@/api/upload";
import {UploadFileResponse} from "@/api/upload/type.ts";
import {TextMsg, SendMsg, MsgType, SendMsgResponse, ShowMsg, MessageListResponse} from "@/api/message/type.ts";
import {reqMessageList, reqSendMsg} from "@/api/message";

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
    const img: UploaderFileListItem = sendImgList.value[0]
    const file: File = new File([img.file as any], <string>img.file?.name, {type: img.file?.type})
    const formData = new FormData()
    formData.append('file', file)
    const resp: UploadFileResponse = await reqUploadFile(formData)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }

    // TODO 发送消息业务
}

// 发送文本消息
const content = ref<string>('')
const sendMsg = async () => {
    const msgBody: TextMsg = {
        content: content.value
    }
    const msg: SendMsg = {
        roomId: roomId.value,
        msgType: MsgType.TEXT,
        body: msgBody
    }
    const resp: SendMsgResponse = await reqSendMsg(msg)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    msgList.value.push(resp.data)
    content.value = ''
}

// 当页面渲染后, 拿到数据
const route = useRoute()
const friendInfo = ref<UserInfo>({} as UserInfo)
const roomId = ref<number>(Number(useRoute().query.roomId))
const msgList = ref<ShowMsg[]>([] as ShowMsg[])

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
const cursor = ref<string>('')
const isLastPage = ref<boolean>(false)
const getMsgList = async () => {
    if (isLastPage.value) {
        return
    }
    const resp: MessageListResponse = await reqMessageList(roomId.value, cursor.value)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    msgList.value.unshift(...resp.data.list.reverse())
    cursor.value = resp.data.cursor
    isLastPage.value = resp.data.isLast
}
getMsgList()

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
    background-color: #F0F0F0;
    min-height: calc(100vh - 8% - 42px);
    .message-panel {
        //height: 10000px;
    }
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
</style>