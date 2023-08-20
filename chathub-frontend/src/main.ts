import { createApp } from 'vue'
import App from './App.vue'
import '@/styles/reset.scss'
import router from '@/router'
import pinia from '@/pinia'
import { Notify } from 'vant'
import 'vant/es/notify/style'
import { Icon } from 'vant';
import { Popover } from 'vant'
import '@/permission.ts'
import { ImagePreview } from 'vant';
import useSseStore from "@/pinia/modules/sse";

const app = createApp(App)
app.use(router)
app.use(pinia)
app.use(Notify)
app.use(Icon)
app.use(Popover)
app.use(ImagePreview)
app.mount('#app')

// 全局监听页面刷新
window.addEventListener('beforeunload', () => {
    const sseStore = useSseStore()
    sseStore.removeSseConnection()
})