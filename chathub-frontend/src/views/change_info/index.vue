<template>
    <van-nav-bar
        title="修改信息"
        left-arrow
        @click-left="onClickLeft"
    />
    <van-field name="uploader" label="头像" center>
        <template #input>
            <van-uploader v-model="imgList" reupload max-count="1" :deletable="false"/>
        </template>
    </van-field>
    <van-field v-model="updateUserInfo.nickName" label="昵称" placeholder="请输入昵称" />
    <div class="button">
        <van-button type="default" block @click="revoke">撤销修改</van-button>
        <van-button type="primary" block @click="update">提交修改</van-button>
    </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import useUserStore from "@/pinia/modules/user";
import {showNotify, UploaderFileListItem} from "vant";
import {UploadFileResponse} from "@/api/upload/type.ts";
import {reqUploadFile} from "@/api/upload";
import {UserInfo} from "@/api/user/type.ts";

const onClickLeft = () => history.back();

// 修改后的信息
const userStore = useUserStore();
const updateUserInfo = ref<UserInfo>({
    account: "",
    avatarUrl: "",
    becomeFriendTime: "",
    friendCount: 0,
    groupCount: 0,
    id: 0,
    isFriend: false,
    isOnline: false,
    level: 0,
    nickName: "",
    phone: "",
    roomId: 0
})
updateUserInfo.value = Object.assign({}, userStore.userInfo);

// 头像
const imgList = ref<UploaderFileListItem[]>([
    {
        url: userStore.userInfo.avatarUrl,
    }
])

// 撤销修改
const revoke = () => {
    updateUserInfo.value = Object.assign({}, userStore.userInfo);
    imgList.value = [
        {
            url: userStore.userInfo.avatarUrl,
        }
    ]
}

// 提交修改
const update = async () => {
    if (updateUserInfo.value.nickName === userStore.userInfo.nickName &&
        imgList.value[0].url === userStore.userInfo.avatarUrl) {
        return
    }
    if (updateUserInfo.value.nickName === '') {
        showNotify({type: 'danger', message: '昵称不能为空!'})
        return
    }

    // 判断如果图片的url属性为undefined, 则说明需要先上传图片
    const rawImg: UploaderFileListItem = imgList.value[0]
    let avatarUrl: string = rawImg.url === undefined ? '' : rawImg.url
    if (rawImg.url === undefined) {
        // 上传图片
        const file: File = new File([rawImg.file as any], <string>rawImg.file?.name, {type: rawImg.file?.type})
        const formData = new FormData()
        formData.append('file', file)
        const resp: UploadFileResponse = await reqUploadFile(formData)
        if (resp.statusCode !== 0) {
            showNotify({type: 'danger', message: '上传图片失败!'})
        }
        avatarUrl = resp.data
    }
    // 更新用户信息
    updateUserInfo.value.avatarUrl = avatarUrl
    const resp = await userStore.updateUserInfo(updateUserInfo.value)
    if (resp.statusCode !== 0) {
        showNotify({type: 'danger', message: '更新用户信息失败!'})
    }
    showNotify({type: 'success', message: '更新用户信息成功!'})
}
</script>

<style scoped lang="scss">
.button {
    padding: 0 10px;
    margin-top: 15px;
    display: flex;
    align-items: center;
    justify-content: center;
}
::v-deep(.van-image__img) {
    border-radius: 50%;
}
</style>