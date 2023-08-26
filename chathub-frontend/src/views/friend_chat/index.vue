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
            <div v-else-if="index == 0" class="time">
                {{formatTime(msg.message.sendTime)}}
            </div>
            <div v-if="msg.fromUser.id == userStore.userInfo.id" class="right">
                <div>
                    <div v-if="msg.message.msgType == MsgType.TEXT" class="message">
                        {{msg.message.body.content}}
                    </div>
                    <div v-else-if="msg.message.msgType == MsgType.IMG">
                        <img :src="msg.message.body.imgMsg?.url" alt="img-msg" :style="{height: msg.message.body.imgMsg?.height + 'px', width: msg.message.body.imgMsg?.width + 'px'}" @click="preview(msg.message.body.imgMsg?.url as string)">
                    </div>
                    <div v-else-if="msg.message.msgType == MsgType.FILE" class="file-message">
                        <img :src="generateFileIcon(msg.message.body.fileMsg?.fileName as string)" alt="other-type" class="suffix-file-img">
                        <div class="file-info">
                            <span>{{msg.message.body.fileMsg?.fileName}}</span>
                            <span style="color: #7f7f7f; font-size: 14px; margin-top: 2px;">{{calcMB(msg.message.body.fileMsg?.size as number)}} MB</span>
                        </div>
                        <svg @click="downloadFile(msg.message.body.fileMsg?.url as string, msg.message.body.fileMsg?.fileName as string)" x="1691162385128" class="icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" p-id="3185" width="24" height="24"><path d="M828.975746 894.125047 190.189132 894.125047c-70.550823 0-127.753639-57.18542-127.753639-127.752616L62.435493 606.674243c0-17.634636 14.308891-31.933293 31.93227-31.933293l63.889099 0c17.634636 0 31.93227 14.298658 31.93227 31.933293l0 95.821369c0 35.282574 28.596292 63.877843 63.87682 63.877843L765.098927 766.373455c35.281551 0 63.87682-28.595268 63.87682-63.877843l0-95.821369c0-17.634636 14.298658-31.933293 31.943526-31.933293l63.877843 0c17.634636 0 31.933293 14.298658 31.933293 31.933293l0 159.699212C956.729385 836.939627 899.538849 894.125047 828.975746 894.125047L828.975746 894.125047zM249.938957 267.509636c12.921287-12.919241 33.884738-12.919241 46.807049 0l148.97087 148.971893L445.716876 94.89323c0-17.634636 14.300704-31.94762 31.933293-31.94762l63.875796 0c17.637706 0 31.945573 14.312984 31.945573 31.94762l0 321.588299 148.97087-148.971893c12.921287-12.919241 33.875528-12.919241 46.796816 0l46.814212 46.818305c12.921287 12.922311 12.921287 33.874505 0 46.807049L552.261471 624.930025c-1.140986 1.137916-21.664416 13.68365-42.315758 13.69286-20.87647 0.010233-41.878806-12.541641-43.020816-13.69286L203.121676 361.13499c-12.922311-12.933567-12.922311-33.884738 0-46.807049L249.938957 267.509636 249.938957 267.509636z" fill="#7f7f7f" p-id="3186"></path></svg>
                    </div>
                </div>
                <img :src="userStore.userInfo.avatarUrl" alt="user-avatar" class="user-avatar">
            </div>
            <div v-else class="left">
                <img :src="friendInfo.avatarUrl" alt="user-avatar" class="user-avatar">
                <div>
                    <div v-if="msg.message.msgType == MsgType.TEXT" class="message">
                        {{msg.message.body.content}}
                    </div>
                    <div v-else-if="msg.message.msgType == MsgType.IMG">
                        <img :src="msg.message.body.imgMsg?.url" alt="img-msg" :style="{height: msg.message.body.imgMsg?.height + 'px', width: msg.message.body.imgMsg?.width + 'px'}" @click="preview(msg.message.body.imgMsg?.url as string)">
                    </div>
                    <div v-else-if="msg.message.msgType == MsgType.FILE" class="file-message">
                        <img :src="generateFileIcon(msg.message.body.fileMsg?.fileName as string)" alt="other-type" class="suffix-file-img">
                        <div class="file-info">
                            <span>{{msg.message.body.fileMsg?.fileName}}</span>
                            <span style="color: #7f7f7f; font-size: 14px; margin-top: 2px;">{{calcMB(msg.message.body.fileMsg?.size as number)}} MB</span>
                        </div>
                        <svg @click="downloadFile(msg.message.body.fileMsg?.url as string, msg.message.body.fileMsg?.fileName as string)" x="1691162385128" class="icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" p-id="3185" width="24" height="24"><path d="M828.975746 894.125047 190.189132 894.125047c-70.550823 0-127.753639-57.18542-127.753639-127.752616L62.435493 606.674243c0-17.634636 14.308891-31.933293 31.93227-31.933293l63.889099 0c17.634636 0 31.93227 14.298658 31.93227 31.933293l0 95.821369c0 35.282574 28.596292 63.877843 63.87682 63.877843L765.098927 766.373455c35.281551 0 63.87682-28.595268 63.87682-63.877843l0-95.821369c0-17.634636 14.298658-31.933293 31.943526-31.933293l63.877843 0c17.634636 0 31.933293 14.298658 31.933293 31.933293l0 159.699212C956.729385 836.939627 899.538849 894.125047 828.975746 894.125047L828.975746 894.125047zM249.938957 267.509636c12.921287-12.919241 33.884738-12.919241 46.807049 0l148.97087 148.971893L445.716876 94.89323c0-17.634636 14.300704-31.94762 31.933293-31.94762l63.875796 0c17.637706 0 31.945573 14.312984 31.945573 31.94762l0 321.588299 148.97087-148.971893c12.921287-12.919241 33.875528-12.919241 46.796816 0l46.814212 46.818305c12.921287 12.922311 12.921287 33.874505 0 46.807049L552.261471 624.930025c-1.140986 1.137916-21.664416 13.68365-42.315758 13.69286-20.87647 0.010233-41.878806-12.541641-43.020816-13.69286L203.121676 361.13499c-12.922311-12.933567-12.922311-33.884738 0-46.807049L249.938957 267.509636 249.938957 267.509636z" fill="#7f7f7f" p-id="3186"></path></svg>
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
            <input type="file" style="display: none" ref="selectFile" @change="handleFileChange" />
            <van-icon name="paid" size="1.8em" @click="openSelectFile"></van-icon>
        </div>
        <van-button type="primary" block style="height: 2.1em;width: 6em;" :disabled="content.length == 0" @click="sendMsg">发送</van-button>
    </div>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {nextTick, onMounted, ref, watch} from "vue";
import {GetUserInfoResponse, UserInfo} from "@/api/user/type.ts";
import {reqGetUserInfoById} from "@/api/user";
import {showImagePreview, showNotify, UploaderFileListItem} from "vant";
import {getEmoji} from "@/utils/emoji.ts";
import {reqUploadFile} from "@/api/upload";
import {UploadFileResponse} from "@/api/upload/type.ts";
import {FileMsg, ImgMsg, MsgType, SendMsg, SendMsgResponse, TextMsg} from "@/api/message/type.ts";
import {reqSendMsg} from "@/api/message";
import useMsgStore from "@/pinia/modules/message";
import {WS} from "@/utils/websocket";
import {buildWSUrl} from "@/utils/websocket/build_url.ts";
import useUserStore from "@/pinia/modules/user";
import {formatTime, isSameMinute} from "@/utils/time_format.ts";
import {calcMB, generateFileIcon} from "@/utils/file.ts";

// 返回上一页
const router = useRouter()
const goBack = () => {
    router.push('/home/message')
    ws.closeConnect()
}

// 发送表情包
const emojiList = getEmoji()
const showEmojiPopover = ref<boolean>(false)
const addEmoji = (emoji: string) => {
    content.value += emoji
}

// 发送图片
const sendImgList = ref<UploaderFileListItem[]>([] as UploaderFileListItem[])
const mxImgSize = ref<number>(1024 * 1024 * 5)
const overMxImgSize = () => {
    showNotify({type: 'danger', message: '图片大小不能超过5MB！'})
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
const preview = (url: string) => {
    showImagePreview({
        images: [url],
        startPosition: 0
    })
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
            messagePanel.value.scrollTo({
                top: messagePanel.value.scrollHeight,
                behavior: 'smooth'
            })
        }
    })
}

// 发送文件
const selectFile = ref<HTMLElement>()
const openSelectFile = () => {
    selectFile.value?.click()
}
const handleFileChange = async (e: any) => {
    if (e.target.files.length == 0) {
        showNotify({type: 'danger', message: '请选择一个文件！'})
    }
    const rawFile: File = e.target.files[0]
    if (rawFile.size > 1024 * 1024 * 1024) {
        showNotify({type: 'danger', message: '文件大小不能超过1GB！'})
        return
    }
    const formData = new FormData()
    formData.append('file', rawFile)
    const resp: UploadFileResponse = await reqUploadFile(formData)
    if (resp.statusCode != 0) {
        sendImgList.value = []
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    const msgBody: FileMsg = {
        size: rawFile.size,
        fileName: rawFile.name,
        url: resp.data
    }
    const msg: SendMsg = {
        roomId: msgStore.roomId,
        msgType: MsgType.FILE,
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
// 下载文件
const downloadFile = async (url: string, filename: string): Promise<void> => {
    const response = await fetch(url)
    const blob = await response.blob()
    const objectUrl = URL.createObjectURL(blob)

    const link = document.createElement('a')
    link.href = objectUrl
    link.download = filename
    document.body.appendChild(link)
    link.click()

    URL.revokeObjectURL(objectUrl)
    document.body.removeChild(link)
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
const ws: WS = new WS(buildWSUrl(userStore.token, msgStore.roomId))

// 让页面渲染完成后, 滚动到底部
const messagePanel = ref<HTMLElement>()
onMounted(async () => {
    msgStore.initData()
    await getMsgList()
    if (messagePanel.value) {
        messagePanel.value.addEventListener('scroll', loadMoreMsg, true)
        messagePanel.value.scrollTop = messagePanel.value.scrollHeight
    }
    // 选中类为class="suffix-file-img"的元素
    const suffixFileImgList = document.getElementsByClassName('suffix-file-img')
    for (let i = 0; i < suffixFileImgList.length; i++) {
        const img = suffixFileImgList[i] as HTMLImageElement
        img.onerror = () => {
            img.src = (import.meta as any).env.VITE_STATIC_ASSETS_PATH + 'images/file/OTHER.png'
        }
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
                // 让滚动条用smooth的方式滚动到底部
                messagePanel.value.scrollTo({
                    top: messagePanel.value.scrollHeight,
                    behavior: 'smooth'
                })
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
    overflow-y: auto;
    //scroll-behavior: smooth;
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
        .file-message {
            display: flex;
            align-items: center;
            border-radius: 8px 8px 8px 8px;
            background-color: white;
            padding: 10px;
            img {
                width: 40px;
                height: 40px;
            }
            .file-info {
                max-width: 180px;
                word-break: break-all;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                margin-left: 10px;
                margin-right: 10px;
            }
            svg:hover {
                cursor: pointer;
            }
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
        .file-message {
            display: flex;
            align-items: center;
            border-radius: 8px 8px 8px 8px;
            background-color: white;
            padding: 10px;
            img {
                width: 40px;
                height: 40px;
            }
            .file-info {
                max-width: 180px;
                word-break: break-all;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                margin-left: 10px;
                margin-right: 10px;
            }
            svg:hover {
                cursor: pointer;
            }
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