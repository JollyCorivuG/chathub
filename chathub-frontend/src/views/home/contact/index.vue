<template>
    <van-tabs v-model:active="selected">
        <van-tab title="好友" name="friend">
            <van-index-bar>
                <div v-for="(item, index) in friendListMap" :key="index">
                    <van-index-anchor :index="item[0]" />
                    <FriendInfo v-for="(friend, index) in item[1]" :key="index" :friend="friend"></FriendInfo>
                </div>
                <van-back-top />
            </van-index-bar>
            <div style="height: 50px;"></div>
        </van-tab>
        <van-tab title="群组" name="group">
            <van-collapse v-model="activeName" accordion>
                <van-collapse-item title="我管理的群聊" name="manage">
                    <GroupInfoView v-for="(group, index) in manageGroupList" :key="index" :group="group"></GroupInfoView>
                </van-collapse-item>
                <van-collapse-item title="我加入的群聊" name="join">
                    <GroupInfoView v-for="(group, index) in joinGroupList" :key="index" :group="group"></GroupInfoView>
                </van-collapse-item>
            </van-collapse>
        </van-tab>
    </van-tabs>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import type {UserList} from "@/api/user/type.ts";
import type {FriendListResponse} from "@/api/friend/type.ts";
import {reqFriendList} from "@/api/friend";
import {showNotify} from "vant";
import FriendInfo from "@/views/home/contact/friend_info/index.vue";
import GroupInfoView from "@/views/home/contact/group_info_view/index.vue";
import pinyin from "pinyin";
import type {GroupInfo, GroupListResponse} from "@/api/group/type.ts";
import {reqMyJoinGroup, reqMyManageGroup} from "@/api/group";

// 好友列表和群组俩表切换
let selected = ref<string>('friend')

// 获取好友列表
const friendList = ref<UserList>([])
const friendListMap = ref<Map<string, UserList>>(new Map<string, UserList>())
const initFriendListMap = () => {
    for (let i = 65; i <= 90; i++) {
        friendListMap.value.set(String.fromCharCode(i), [])
    }
}
onMounted(async () => {
    initFriendListMap()
    const resp: FriendListResponse = await reqFriendList()
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    friendList.value = resp.data
    friendList.value.forEach((friend) => {
        const firstLetter = friend.nickName[0]
        let key: string = firstLetter.toUpperCase()
        // 如果是中文字符
        if (firstLetter.match(/[^\x00-\xff]/gi)) {
            key = pinyin(firstLetter)[0][0][0].toUpperCase()
        }
        if (friendListMap.value.has(key)) {
            friendListMap.value.get(key)?.push(friend)
        } else {
            friendListMap.value.set(key, [friend])
        }
    })
})

// 群组列表
const manageGroupList = ref<GroupInfo[]>([])
const joinGroupList = ref<GroupInfo[]>([])
const getGroupList = async () => {
    const resp1: GroupListResponse = await reqMyManageGroup()
    if (resp1.statusCode != 0) {
        showNotify({type: 'danger', message: resp1.statusMsg})
        return
    }
    manageGroupList.value = resp1.data
    const resp2: GroupListResponse = await reqMyJoinGroup()
    if (resp2.statusCode != 0) {
        showNotify({type: 'danger', message: resp2.statusMsg})
        return
    }
    joinGroupList.value = resp2.data
}
getGroupList()

// 群组列表切换
const activeName = ref<string>('')

</script>

<style scoped lang="scss">
</style>