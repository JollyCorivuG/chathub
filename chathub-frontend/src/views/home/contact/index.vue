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
        <van-tab title="群组" name="group">群组列表</van-tab>
    </van-tabs>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import type {UserList} from "@/api/user/type.ts";
import type {FriendListResponse} from "@/api/friend/type.ts";
import {reqFriendList} from "@/api/friend";
import {showNotify} from "vant";
import FriendInfo from "@/views/home/contact/friend_info/index.vue";
import pinyin from "pinyin";

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
</script>

<style scoped lang="scss">
</style>