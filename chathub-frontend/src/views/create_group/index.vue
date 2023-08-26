<template>
    <van-nav-bar
        title="创建群组"
        left-arrow
        @click-left="onClickLeft"
    />
    <div class="container">
        <van-cell-group inset>
            <van-field
                v-model="createGroupParams.name"
                name="群名称"
                label="群名称"
                placeholder="请输入群名称"
                required
                :rules="[{ required: true, message: '请填写用户名' }]"
            />
            <van-field name="uploader" label="群头像" center required>
                <template #input>
                    <van-uploader max-count="1" v-model="groupAvatarList" />
                </template>
            </van-field>
            <van-field name="stepper" label="群人数上限" required>
                <template #input>
                    <van-stepper  v-model="createGroupParams.maxPeopleNum" max="1000" theme="round" button-size="22" :integer="true" />
                </template>
            </van-field>
        </van-cell-group>
        <div style="margin: 16px;">
            <van-button round block type="primary" @click="createGroup">
                创建
            </van-button>
        </div>
    </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {CreateGroupParams, CreateGroupResponse} from "@/api/group/type.ts";
import {showNotify, UploaderFileListItem} from "vant";
import {reqUploadFile} from "@/api/upload";
import {reqCreateGroup} from "@/api/group";

// 创建群组的参数
const createGroupParams = ref<CreateGroupParams>({
    name: '',
    avatar: '',
    maxPeopleNum: 10
})

// 群头像
const groupAvatarList = ref<UploaderFileListItem[]>([])
const onClickLeft = () => {
    history.back()
}

// 创建群组
const valid = (): boolean => {
    if (createGroupParams.value.name.length == 0 || createGroupParams.value.name.length >= 10) {
        return false
    }
    return groupAvatarList.value.length != 0;
}
const createGroup = async () => {
    // 先发送文件到服务器
    if (!valid()) {
        showNotify({type: 'danger', message: '请检查输入参数是否正确!'})
        return
    }
    const groupAvatar: UploaderFileListItem = groupAvatarList.value[0]
    const formData = new FormData()
    const file: File = new File([groupAvatar.file as any], <string>groupAvatar.file?.name, {type: groupAvatar.file?.type})
    formData.append('file', file)
    const resp = await reqUploadFile(formData)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
        return
    }
    createGroupParams.value.avatar = resp.data
    const resp2: CreateGroupResponse = await reqCreateGroup(createGroupParams.value)
    if (resp2.statusCode != 0) {
        showNotify({type: 'danger', message: resp2.statusMsg})
        return
    }
    showNotify({type: 'success', message: '创建成功!'})
    history.back()
}
</script>

<style scoped lang="scss">
.container {
    margin-top: 10px;
    background-color: #F7F8FA;
}
</style>