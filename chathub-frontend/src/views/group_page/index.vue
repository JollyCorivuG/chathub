<template>
    <div class="container">
        <van-nav-bar
            left-arrow
            @click-left="onClickLeft"
        >
            <template #right>
                <van-icon name="ellipsis" size="18" />
            </template>
        </van-nav-bar>
        <div class="basic-info">
            <div class="top">
                <div class="left">
                    <img :src="groupInfo.avatar" alt="group-avatar">
                </div>
                <div class="right">
                    <span class="group-name">{{groupInfo.name}}</span>
                    <span class="group-number">群号: {{groupInfo.number}}</span>
                </div>
            </div>
            <div class="bottom">
                <svg x="1692951003761" class="icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" p-id="6145" id="mx_n_1692951003762" width="16" height="16"><path d="M511.913993 63.989249c-247.012263 0-447.924744 200.912481-447.924744 447.924744s200.912481 447.924744 447.924744 447.924744 447.924744-200.912481 447.924744-447.924744S758.926256 63.989249 511.913993 63.989249zM511.913993 895.677474c-211.577356 0-383.763481-172.186125-383.763481-383.763481 0-211.577356 172.014111-383.763481 383.763481-383.763481s383.763481 172.014111 383.763481 383.763481S723.491349 895.677474 511.913993 895.677474z" fill="#7f7f7f" p-id="6146"></path><path d="M672.05913 511.913993l-159.973123 0L512.086007 288.123635c0-17.717453-14.277171-32.166639-31.994625-32.166639-17.717453 0-31.994625 14.449185-31.994625 32.166639l0 255.956996c0 17.717453 14.277171 31.994625 31.994625 31.994625l191.967747 0c17.717453 0 32.166639-14.277171 32.166639-31.994625C704.053754 526.191164 689.604569 511.913993 672.05913 511.913993z" fill="#7f7f7f" p-id="6147"></path></svg>
                <span class="create-time">{{convertJavaTimeToStringYearMonthDay(groupInfo.createTime)}}</span>
            </div>
        </div>
        <div class="members">
            <van-collapse v-model="activeName" accordion>
                <van-collapse-item name="members" style="padding: 10px">
                    <template #title>
                        <span style="font-size: 16px;">成员概述 ({{groupInfo.peopleNum}}人)</span>
                    </template>
                    <div class="member" style="margin-top: 5px;">
                        <div class="left">
                            <img :src="groupInfo.owner?.avatarUrl" alt="user-avatar">
                            <span>{{groupInfo.owner?.nickName}}</span>
                        </div>
                        <div class="right">
                            <van-tag type="success">群主</van-tag>
                        </div>
                    </div>
                    <div v-for="(member, index) in groupInfo.members " class="member" :key="index">
                        <div class="left">
                            <img :src="member.avatarUrl" alt="user-avatar">
                            <span>{{member.nickName}}</span>
                        </div>
                        <div class="right">
                            <van-tag type="warning">成员</van-tag>
                        </div>
                    </div>
                </van-collapse-item>
            </van-collapse>
        </div>
        <div class="owner">
            <div class="manager">
                管理员
            </div>
            <img :src="groupInfo.owner?.avatarUrl" alt="owner-avatar">
        </div>
        <div class="operator">
            <van-button color="#eba0b3" block style="margin-right: 20px;" @click="inviteFriend" plain>邀请好友</van-button>
            <van-button type="primary" block @click="goGroupChat">进入群聊</van-button>
<!--            <van-button type="danger" v-if="groupInfo.owner?.id != userStore.userInfo.id" block plain>退出群聊</van-button>-->
<!--            <van-button type="danger" v-else block plain>解散群聊</van-button>-->
        </div>
        <van-dialog v-model:show="inviteDialogVis" title="邀请好友" show-cancel-button @confirm="confirmInviteFriends">
            <van-checkbox-group v-model="selectedFriends">
                <van-cell-group inset>
                    <van-cell
                        v-for="(friend, index) in friendList"
                        clickable
                        @click="toggle(friend.id, index)"
                    >
                        <template #title>
                            <div class="friend-info">
                                <img :src="friend.avatarUrl" alt="user-avatar">
                                <span>{{friend.nickName}}</span>
                            </div>
                        </template>
                        <template #right-icon>
                            <van-checkbox
                                v-if="isAlreadyInGroup(friend.id)"
                                :name="friend.id"
                                v-model="checked"
                                :ref="el => checkboxRefs[index] = el as CheckboxInstance"
                                @click.stop
                                :disabled="true"
                                :bind-group="false"
                            />
                            <van-checkbox
                                v-else
                                :name="friend.id"
                                :ref="el => checkboxRefs[index] = el as CheckboxInstance"
                                @click.stop
                            />
                        </template>
                    </van-cell>
                </van-cell-group>
            </van-checkbox-group>
        </van-dialog>
    </div>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router"
import {ref} from "vue";
import {CommonResponse, GroupInfo, GroupInfoResponse} from "@/api/group/type.ts";
import {reqGroupInfo, reqInviteFriend} from "@/api/group";
import {CheckboxInstance, showNotify} from "vant";
import {UserInfo} from "@/api/user/type.ts";
import {convertJavaTimeToStringYearMonthDay} from "@/utils/time_format.ts";
import useUserStore from "@/pinia/modules/user";
import {FriendListResponse} from "@/api/friend/type.ts";
import {reqFriendList} from "@/api/friend";

const onClickLeft = () => {
    history.back()
}

// 群组信息
const route = useRoute()
const groupInfo = ref<GroupInfo>({
    id: 0,
    number: '',
    name: '',
    avatar: '',
    peopleNum: 0,
    owner: {
        id: 0,
        phone: '',
        account: '',
        nickName: '',
        avatarUrl: '',
        level: 0,
        friendCount: 0,
        groupCount: 0,
        isOnline: false,
        isFriend: false,
        becomeFriendTime: '',
        roomId: 0
    } as UserInfo,
    members: [],
    roomId: 0,
    createTime: ''
})
const getGroupInfo = async () => {
    const resp: GroupInfoResponse = await reqGroupInfo(Number(route.query.id as string))
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    groupInfo.value = resp.data
}
getGroupInfo()

// 群成员
const activeName = ref<string>('')


// 邀请好友加入
const checked = ref(true)
const friendList = ref<UserInfo[]>([])
const getFriendList = async () => {
    const resp: FriendListResponse = await reqFriendList()
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    friendList.value = resp.data
}
getFriendList()
const isAlreadyInGroup = (userId: number): boolean => {
    return groupInfo.value.members?.some((member: UserInfo) => {
        return member.id == userId
    }) as boolean || userId == groupInfo.value.owner?.id
}
const inviteDialogVis = ref<boolean>(false)
const selectedFriends = ref<number[]>([])
const inviteFriend = () => {
    inviteDialogVis.value = true
}
const checkboxRefs = ref<CheckboxInstance[]>([]);
const toggle = (userId: number, index: number) => {
    if (isAlreadyInGroup(userId)) {
        return
    }
    checkboxRefs.value[index].toggle()
}
const confirmInviteFriends = async () => {
    const resp: CommonResponse = await reqInviteFriend(groupInfo.value.id, selectedFriends.value)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    showNotify({type: 'success', message: '邀请成功！'})
    selectedFriends.value = []
}

// 进入群组群聊
const router = useRouter()
const goGroupChat = () => {
    router.push({
        path: '/group_chat',
        query: {
            groupId: groupInfo.value.id,
            roomId: groupInfo.value.roomId
        }
    })
}
</script>

<style scoped lang="scss">
.container {
    height: 100%;
    background-color: #f5f5f5;
    overflow: auto;
    .basic-info {
        background-color: white;
        .top {
            display: flex;
            align-items: center;
            padding: 15px;
            .left {
                width: 70px;
                height: 70px;
                border-radius: 50%;
                overflow: hidden;
                img {
                    width: 100%;
                    height: 100%;
                }
                margin-right: 15px;
            }
            .right {
                display: flex;
                flex-direction: column;
                justify-content: center;
                .group-name {
                    font-size: 18px;
                    font-weight: bold;
                    margin-bottom: 8px;
                }
                .group-number {
                    font-size: 14px;
                }
            }

        }
        .bottom {
            padding: 0 0 10px 10px;
            display: flex;
            align-items: center;
            .icon {
                margin-right: 8px;
            }
            .create-time {
                font-size: 14px;
                color: #7f7f7f;
            }
        }
    }
    .members {
        background-color: white;
        ::v-deep(.van-cell) {
            padding: 0;
        }
        ::v-deep(.van-collapse-item__content) {
            padding: 0;
        }
        margin-top: 20px;
        .member {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            .left {
                display: flex;
                align-items: center;
                img {
                    width: 40px;
                    height: 40px;
                    border-radius: 50%;
                    margin-right: 10px;
                }
                span {
                    color: black;
                    font-size: 16px;
                }
            }
        }
    }
    .owner {
        margin-top: 20px;
        padding: 5px 10px;
        background-color: white;
        display: flex;
        align-items: center;
        justify-content: space-between;
        img {
            width: 40px;
            height: 40px;
            border-radius: 50%;
        }
    }
    .operator {
        margin-top: 20px;
        padding: 0 10px;
        display: flex;
        align-items: center;
        justify-content: space-evenly;
    }
    .friend-info {
        display: flex;
        align-items: center;
        img {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            margin-right: 10px;
        }
    }
}
</style>