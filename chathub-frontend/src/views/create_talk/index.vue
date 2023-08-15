<template>
    <div class="container">
        <van-nav-bar
            title="写说说"
            left-text="返回"
            left-arrow
            @click-left="onClickLeft">
            <template #right>
                <van-button type="primary" style="height: 30px;" :disabled="params.content == '' && imgList.length == 0" @click="postTalk">发表</van-button>
            </template>
        </van-nav-bar>
        <div class="content">
            <div class="text">
                <van-field
                    v-model="params.content"
                    rows="4"
                    autosize
                    type="textarea"
                    maxlength="300"
                    placeholder="分享新鲜事..."
                />
            </div>
            <div class="extra">
                <van-uploader v-model="imgList" multiple upload-icon="photo-o" :max-count="9" :preview-size="siz" />
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
// 返回上一页
import {CreateTalkParams, CreateTalkResponse, ExtraInfo, ExtraType, ImageInfo} from "@/api/trend/type.ts";
import {onMounted, ref} from "vue";
import {reqCreateTalk} from "@/api/trend";
import {showNotify, UploaderFileListItem} from "vant";
import {useRouter} from "vue-router";
import {reqUploadFile} from "@/api/upload";
import {UploadFileResponse} from "@/api/upload/type.ts";


const onClickLeft = () => {
    history.back()
}

// 发布说说的相关内容
const router = useRouter()
const params = ref<CreateTalkParams>({
    content: '',
    extra: []
})
const postTalk = async () => {
    await sendImg()
    const resp: CreateTalkResponse = await reqCreateTalk(params.value)
    if (resp.statusCode != 0) {
        showNotify({type: 'danger', message: resp.statusMsg})
    }
    showNotify({type: 'success', message: '发布成功!'})
    console.log(resp)
    // 直接跳转到动态页面
    await router.push({
        path: '/home/trend',
    })
}

// 可以发送图片
const imgList = ref<UploaderFileListItem[]>([])
const sendMultipleUploadReq = async (dataList: FormData[]): Promise<UploadFileResponse[]> => {
    return await Promise.all(dataList.map(data => reqUploadFile(data)));
}
const sendImg = async () => {
    if (imgList.value.length == 0) {
        return
    }
    const dataList: FormData[] = []
    imgList.value.forEach(item => {
        const formData = new FormData()
        const file: File = new File([item.file as any], <string>item.file?.name, {type: item.file?.type})
        formData.append('file', file)
        dataList.push(formData)
    })
    console.log(dataList)
    const resp: UploadFileResponse[] = await sendMultipleUploadReq(dataList)
    // 添加到params.extra中
    resp.forEach(item => {
        const extraInfo: ExtraInfo = {
            type: ExtraType.IMG,
            data: {
                url: item.data
            } as ImageInfo
        }
        params.value.extra.push(extraInfo)
    })
}
// 渲染完成后
const siz = ref<number>(0)
onMounted(() => {
    const extraWidth = document.getElementsByClassName('extra')[0].clientWidth
    siz.value = (extraWidth - 48) / 3
})
</script>

<style scoped lang="scss">
.container {
    background: #F4F4F4;
    height: 100%;
    width: 100%;
    .content {
        margin-top: 15px;
        background-color: #FFFFFF;
        .extra {
            width: 100%;
            padding: 7px 8px 7px 16px;
        }
    }
}
</style>