<template>
    <div class="add-top">
        <div class="left">
            <van-icon name="arrow-left" size="1.8em" color="#fff" @click="goBack"></van-icon>
        </div>
        <div class="middle">
            <div class="search-person" :class="{active: searchType == 0}" @click="searchPerson">加人</div>
            <div class="search-group" :class="{active: searchType == 1}" @click="searchGroup">加群</div>
        </div>
        <div class="right">
            <van-icon name="search" size="1.8em" color="#fff"></van-icon>
        </div>
    </div>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {ref} from "vue";
import {onMounted} from "vue";

// 返回上一页
const router = useRouter()
const route = useRoute()
const lastPage = ref<string>('')
onMounted(() => {
    lastPage.value = route.query.from as string
})
const goBack = () => {
    router.push(lastPage.value)
}

// 点击找人按钮或者找群按钮
let searchType = ref<number>(0)
const searchPerson = () => {
    searchType.value = 0
    router.push({
        path: '/add/friend',
        query: {
            from: lastPage.value
        }
    })
}
const searchGroup = () => {
    searchType.value = 1
    router.push({
        path: '/add/group',
        query: {
            from: lastPage.value
        }
    })
}
</script>

<style scoped lang="scss">
.add-top {
    width: 100%;
    position: fixed;
    z-index: 999;

    // 实现#00D2FC到#27B7FC的渐变色
    background: linear-gradient(90deg, #00D2FC 0%, #27B7FC 100%);
    height: 8%;
    padding: 0 15px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .middle {
        display: flex;
        align-items: center;
        font-size: 17px;
        color: white;
        .search-person {
            padding: 8px 8px 8px 15px;
            border: 1px solid white;
            border-right: none;
            // 左边框圆角
            border-top-left-radius: 20px;
            border-bottom-left-radius: 20px;

            // 如果被选中, 背景颜色为白色, 字体颜色为深蓝色
            &.active {
                background-color: white;
                color: #00D2FC;
            }
        }
        .search-group {
            padding: 8px 15px 8px 8px;
            border: 1px solid white;
            border-top-right-radius: 20px;
            border-bottom-right-radius: 20px;
            // 如果被选中, 背景颜色为白色, 字体颜色为深蓝色
            &.active {
                background-color: white;
                color: #00D2FC;
            }
        }
    }
}
</style>