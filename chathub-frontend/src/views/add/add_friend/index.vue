<template>
    <van-search
        v-model="keyWord"
        show-action
        placeholder="请输入用户昵称 / 账号 / 手机号"
        @search="searchUser"
    >
        <template #action>
            <div @click="searchUser">搜索</div>
        </template>
    </van-search>

    <van-list
        v-if="keyWord != '' && isSearch"
        v-model:loading="loading"
        :finished="finished"
        @load="onLoadSearchResult"
        v-model:error="error"
        error-text="请求失败，点击重新加载"
    >
        <van-cell v-for="user in userList" :key="user.id" is-link>
            <template #title>
                <div class="user-info">
                    <div class="left">
                        <van-image width="50px" height="50px" :src="user.avatarUrl" round/>
                    </div>
                    <div class="right">
                        <div class="nickname" style="color: black;font-size: 18px;">
                            <span v-html="highlightedResults(user.nickName)"></span>
                        </div>
                        <div class="account">
                            账号:<span v-html="highlightedResults(user.account)"></span>
                        </div>
                        <div class="phone">
                            手机号:<span v-html="highlightedResults(user.phone)"></span>
                        </div>
                    </div>
                </div>
            </template>
            <template #right-icon>
                <van-button type="default" size="small" :disabled="user.isFriend" @click="addFriend(user.id)">{{ user.isFriend ? '已添加': '添加' }}</van-button>
            </template>
        </van-cell>
    </van-list>
    <van-empty image-size="200" image="search" v-else>
        <template #description>
            <div style="color: #7f7f7f;font-size: 17px;">搜索其他用户</div>
        </template>
    </van-empty>
    <van-dialog v-model:show="isDescriptionShow" title="描述信息" show-cancel-button @confirm="confirmAddFriend">
        <van-field
            v-model="addFriendParams.description"
            rows="2"
            autosize
            type="textarea"
            maxlength="50"
            show-word-limit
        />
    </van-dialog>
</template>

<script setup lang="ts">
import {ref, watch, computed} from "vue";
import {showNotify} from "vant";
import type {SearchUserResponse, UserList} from "@/api/user/type.ts";
import {reqSearchUser} from "@/api/user";
import useUserStore from "@/pinia/modules/user";
import type {AddFriendParams, CommonResponse} from "@/api/friend/type";
import {reqSendFriendApply} from "@/api/friend";

// 搜索框
let keyWord = ref<string>('')
// 检测keyWord的变化, 当keyWord发送变化时, 将isSearch设置为false
watch(keyWord, () => {
    isSearch.value = false
})

// 查询的用户列表
let loading = ref<boolean>(false)
let finished = ref<boolean>(false)
const userList = ref<UserList>([])
const onLoadSearchResult = async () => {
    console.log(userList.value)
    const resp: SearchUserResponse = await reqSearchUser(keyWord.value, currentPage.value)
    if (resp.statusCode != 0) {
        error.value = true
        return
    }
    // 加载状态结束
    loading.value = false
    const addUserList: UserList = resp.data
    if (addUserList.length == 0) {
        finished.value = true
        return
    }
    // 将新的用户列表添加到原有的用户列表中
    userList.value = userList.value.concat(addUserList)
    currentPage.value++
}

// 设置关键词高亮
const highlightedResults = computed(() => {
    return (content: string) => {
        // 将content中的关键词高亮, 颜色#27B7FC
        return content.replace(keyWord.value, `<span style="color: #27B7FC">${keyWord.value}</span>`)
    }
});

// 搜索业务
let error = ref<boolean>(false)
let currentPage = ref<number>(1)
let isSearch = ref<boolean>(false)
const searchUser = () => {
    currentPage.value = 1
    error.value = false
    isSearch.value = true
    userList.value = []
    finished.value = false
    loading.value = false
    if (keyWord.value == '') {
        showNotify({type: 'danger', message: '搜索内容不能为空!'})
        return
    }
}

// 添加好友业务
const addFriendParams = ref<AddFriendParams>({
    toUserId: 0,
    description: ''
})
let isDescriptionShow = ref<boolean>(false)
const userStore = useUserStore()
const addFriend = (userId: number) => {
    addFriendParams.value.description = '我是' + userStore.userInfo.nickName
    addFriendParams.value.toUserId = userId
    isDescriptionShow.value = true
}
const confirmAddFriend = async () => {
    const resp: CommonResponse = await reqSendFriendApply(addFriendParams.value)
    if (resp.statusCode == 0) {
        showNotify({type: 'success', message: '发送好友申请成功!'})
    } else {
        showNotify({type: 'danger', message: resp.statusMsg})
    }
}

</script>

<style scoped lang="scss">
.van-cell {
    align-items: center;
}
.user-info {
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
</style>