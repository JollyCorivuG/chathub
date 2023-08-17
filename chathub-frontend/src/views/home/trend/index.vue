<template>
    <div class="container">
        <van-pull-refresh v-model="refreshLoading" @refresh="onRefresh">
            <van-list v-model:loading="loading" :finished="isLast" @load="getTalkList">
                <div v-for="(talk, index) in talkList" :key="index" class="talk-panel">
                    <div class="top">
                        <div class="left">
                            <div class="avatar">
                                <van-image width="40px" height="40px" :src="talk.author.avatarUrl" round/>
                            </div>
                            <div class="info">
                                <div class="nickname" style="font-weight: bold">{{ talk.author.nickName }}</div>
                                <div class="time">{{ formatTime(talk.createTime) }}</div>
                            </div>
                        </div>
                        <van-icon name="ellipsis" color="#7F7F7F" size="24px"/>
                    </div>
                    <div class="content">
                        {{talk.content}}
                    </div>
                    <div class="extra">
                        <img v-for="(img, index1) in talk.extra" :key="index" :src="img.data.url" alt="photo" @click="previewImg(talk.extra, index1)" />
                    </div>
                    <div class="action">
                        <van-icon v-if="talk.isLike" name="like" color="#FF0000" size="24px" @click="doLike(talk, false)"/>
                        <van-icon  v-else name="like-o" color="#7F7F7F" size="24px" @click="doLike(talk, true)" />
                        <van-icon name="comment-o" color="#7F7F7F" size="24px" style="margin-left: 10px" />
                        <van-icon name="share-o" color="#7F7F7F" size="24px" style="margin-left: 10px" />
                    </div>
                    <div class="like-people">
                        <svg x="1692069586276" class="icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" p-id="2339" width="20" height="20"><path d="M772.2 380.3H653.6s17.6-59 26.5-158.6c4.5-50.5-30.9-98.5-82.8-104-3.2-0.5-6.3-0.7-9.5-0.7-42.3 0-79.2 29.2-86.9 70.5 0 0-12.6 64.7-19.1 88-10.8 38.3-48.6 80.7-76 94.3-20.7 10.3-97.5 10.4-102.2 10.4H199.1c-28.8 0-52.1 23.5-52.1 52.7v421.7c0 29.1 23.3 52.7 52.1 52.7h481.2c77.4 0 143.2-57.2 154.7-134.6l40.3-271.4c9.5-63.8-39.3-121-103.1-121zM309.1 852.8H201V444h108.1v408.8z" fill="#515151" p-id="2340"></path></svg>
                        {{getLikePeople(talk.latestLikeUsers, talk.likeCount)}}
                    </div>
                    <div class="comments" v-if="talk.comments.length != 0">
                        <div v-for="(comment, index2) in talk.comments" :key="index2" class="comment-item">
                            <van-popover v-model:show="showPopover[comment.id]" placement="bottom-start">
                                <div class="reply-panel" style="display: flex; align-items: center;">
                                    <van-field
                                        v-model="replyCommentInfo"
                                        rows="1"
                                        autosize
                                        type="textarea"
                                        placeholder="请输入回复内容..."
                                    />
                                    <svg style="margin-right: 5px;" @click="reply(talk, comment)" x="1692070833722" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3525" width="30" height="30"><path d="M871.04 89.770667L120.064 380.16a51.2 51.2 0 0 0-1.792 94.762667l303.36 130.56 131.072 303.957333a51.2 51.2 0 0 0 94.805333-1.877333l289.792-751.573334a51.2 51.2 0 0 0-66.261333-66.133333z m-41.130667 107.392l-231.978666 601.642666-97.962667-227.114666-3.584-7.338667a85.333333 85.333333 0 0 0-41.045333-37.248l-226.56-97.536 601.173333-232.405333z" fill="#bfbfbf" p-id="3526"></path></svg>
                                </div>

                                <template #reference>
                                    <span style="font-weight: bold">{{comment.sender.nickName}}</span>
                                    <span v-if="comment.replyUserId != 0" style="margin: 0 3px;">回复</span>
                                    <span v-if="comment.replyUserId != 0" style="font-weight: bold">{{comment.replyUser?.nickName}}</span>
                                    <span style="margin-right: 3px;">:</span>
                                    <span>{{comment.content}}</span>
                                </template>
                            </van-popover>
                        </div>
                    </div>
                    <div class="comment">
                        <div class="left">
                            <van-image width="24px" height="24px" :src="userStore.userInfo.avatarUrl" round/>
                        </div>
                        <div class="right">
                            <van-field
                                v-model="commentInfo[index]"
                                rows="1"
                                autosize
                                type="textarea"
                                maxlength="300"
                                placeholder="说点什么吧..."
                                style="background-color: #F4F4F4;padding:3px 7px;"
                            />
                        </div>
                        <div class="post">
                            <svg @click="doComment(talk, index)" x="1692070833722" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3525" width="25" height="25"><path d="M871.04 89.770667L120.064 380.16a51.2 51.2 0 0 0-1.792 94.762667l303.36 130.56 131.072 303.957333a51.2 51.2 0 0 0 94.805333-1.877333l289.792-751.573334a51.2 51.2 0 0 0-66.261333-66.133333z m-41.130667 107.392l-231.978666 601.642666-97.962667-227.114666-3.584-7.338667a85.333333 85.333333 0 0 0-41.045333-37.248l-226.56-97.536 601.173333-232.405333z" fill="#bfbfbf" p-id="3526"></path></svg>
                        </div>
                    </div>
                </div>
            </van-list>
        </van-pull-refresh>
    </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {ExtraInfo, LikeParams, ShowTalkInfo, TalkListResponse} from "@/api/trend/type.ts";
import {reqLike, reqTalkList} from "@/api/trend";
import {showImagePreview, showNotify} from "vant";
import {formatTime} from "@/utils/time_format.ts";
import {getLikePeople} from "@/utils/talk.ts";
import useUserStore from "@/pinia/modules/user";
import {CommonResponse} from "@/api/user/type.ts";
import {PostCommentParams, PostCommentResponse, ShowCommentInfo} from "@/api/comment/type.ts";
import {reqPostComment} from "@/api/comment";

// 说说列表
const talkList = ref<ShowTalkInfo[]>([])
const cursor = ref<string>('')
const pageSize = ref<number>(5)
const isLast = ref<boolean>(false)
const loading = ref<boolean>(false)
const getTalkList = async () => {
    const resp: TalkListResponse = await reqTalkList(pageSize.value, cursor.value)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
    }
    initCommentInfo()
    cursor.value = resp.data.cursor
    isLast.value = resp.data.isLast
    talkList.value = talkList.value.concat(resp.data.list)
    loading.value = false
    refreshLoading.value = false
}

// 下拉刷新
const refreshLoading = ref<boolean>(false)
const onRefresh = async () => {
    isLast.value = false
    cursor.value = ''
    talkList.value = []
}

// 预览图片
const previewImg = (urls: ExtraInfo[], index: number) => {
    showImagePreview({
        images: urls.map(item => item.data.url),
        startPosition: index
    })
}

// 点赞
const doLike = async (talk: ShowTalkInfo, isLike: boolean) => {
    const likeParams: LikeParams = {
        talkId: talk.id,
        isLike: isLike
    }
    const resp: CommonResponse = await reqLike(likeParams)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
    }
    talk.isLike = isLike
    if (isLike) {
        talk.likeCount++
        talk.latestLikeUsers.unshift(userStore.userInfo)
    } else {
        talk.likeCount--
        // 如果是取消点赞，就删除最新点赞用户列表中的自己, 如果列表中没有自己，就不删除
        const index = talk.latestLikeUsers.findIndex(item => item.id == userStore.userInfo.id)
        if (index != -1) {
            talk.latestLikeUsers.splice(index, 1)
        }
    }
}

// 评论
const userStore = useUserStore()
const commentInfo = ref<string[]>([])
const initCommentInfo = () => {
    commentInfo.value = []
    for (let i = 0; i < talkList.value.length; i++) {
        commentInfo.value.push('')
    }
}
const commentParams = ref<PostCommentParams>({
    talkId: 0,
    content: '',
    replyUserId: 0
})
const doComment = async (talk: ShowTalkInfo, index: number) => {
    if (commentInfo.value[index] == '') {
        return
    }
    commentParams.value.talkId = talk.id
    commentParams.value.content = commentInfo.value[index]
    commentParams.value.replyUserId = 0
    const resp: PostCommentResponse = await reqPostComment(commentParams.value)
    commentInfo.value[index] = ''
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
    }
    talk.comments.push(resp.data)
}

// 回复评论
const showPopover = ref<boolean[]>([])
const replyCommentInfo = ref<string>('')
const reply = async (talk: ShowTalkInfo, comment: ShowCommentInfo) => {
    if (replyCommentInfo.value == '') {
        return
    }
    commentParams.value.talkId = talk.id
    commentParams.value.content = replyCommentInfo.value
    commentParams.value.replyUserId = comment.sender.id
    const resp: PostCommentResponse = await reqPostComment(commentParams.value)
    replyCommentInfo.value = ''
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
    }
    showPopover.value[comment.id] = false
    talk.comments.push(resp.data)
}


</script>

<style scoped lang="scss">
.container {
    width: 100%;
    height: calc(100% - 50px - 8%);
    overflow: auto;
    background-color: #F1F1F1;
    .talk-panel {
        background-color: #FEFEFE;
        margin-top: 10px;
        padding: 15px;
        .top {
            display: flex;
            justify-content: space-between;
            align-items: center;
            .left {
                display: flex;
                .avatar {
                    margin-right: 5px;
                }
                .info {
                    display: flex;
                    flex-direction: column;
                    justify-content: space-evenly;
                    .nickname {
                        font-size: 16px;
                        color: black;
                    }
                    .time {
                        font-size: 14px;
                        color: #7F7F7F;
                    }
                }
            }
        }
        .content {
            margin-top: 15px;
            font-size: 17px;
            color: black;
            word-break: break-all;
        }
        .extra {
            margin-top: 10px;
            width: 100%;
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            img {
                width: 33%;
                height: auto;
                margin-bottom: 3px;
            }
        }
        .action {
            margin-top: 5px;
            display: flex;
            align-items: center;
            justify-content: flex-end;
        }
        .like-people {
            margin-top: 5px;
            font-size: 16px;
            color: black;
            display: flex;
            align-items: center;
            word-break: break-all;
        }
        .comments {
            margin-top: 8px;
            display: flex;
            flex-direction: column;
            .comment-item {
                &:active {
                    background-color: #F4F4F4;
                }
                margin-top: 5px;
                font-size: 16px;
                color: black;
                display: flex;
                align-items: center;
                word-break: break-all;
            }
        }
        .comment {
            margin-top: 15px;
            display: flex;
            align-items: center;
            .left {
                height: 24px;
            }
            .right {
                margin-left: 6px;
                flex: 1;
                margin-right: 5px;
            }
            .post {
                height: 24px;
            }
        }
    }
}
</style>