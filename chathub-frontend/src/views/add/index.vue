<template>
    <AddTop></AddTop>
    <div class="offset"></div>
    <router-view></router-view>
</template>

<script setup lang="ts">
import AddTop from '@/components/add_top/index.vue'
</script>

<style scoped lang="scss">
.offset {
    height: 8%;
}
</style>