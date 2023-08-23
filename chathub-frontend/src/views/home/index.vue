<template>
    <TopTab></TopTab>
    <div class="offset"></div>
    <router-view></router-view>
    <BottomTabbar></BottomTabbar>
</template>

<script setup lang="ts">
import TopTab from '@/components/top_tab/index.vue'
import BottomTabbar from '@/components/bottom_tabbar/index.vue'
</script>

<style scoped lang="scss">
.offset {
    height: 8%;
}
</style>