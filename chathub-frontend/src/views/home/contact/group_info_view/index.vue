<template>
    <van-cell is-link style="padding: 8px 0;" @click="goGroupInfoPage">
        <template #title>
            <div class="group-info">
                <img :src="group.avatar" alt="group-avatar">
                <span>{{group.name}} <span style="color: #7F7F7F">({{group.peopleNum}})</span></span>
            </div>
        </template>
        <template #right-icon>
        </template>
    </van-cell>
</template>

<script setup lang="ts">
import {GroupInfo} from "@/api/group/type.ts";
import {PropType} from "vue";
import {useRouter} from "vue-router";

const props = defineProps({
    group: {
        type: Object as PropType<GroupInfo>,
        required: true
    }
})

// 点击单元格跳转到群信息页
const router = useRouter()
const goGroupInfoPage = () => {
    router.push({
        path: '/group_page',
        query: {
            id: props.group?.id
        }
    })
}

</script>

<style scoped lang="scss">
.group-info {
    display: flex;
    align-items: center;
    img {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        margin-right: 10px;
    }
}
</style>