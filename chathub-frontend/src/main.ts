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
import {UserInfo} from "@/api/user/type.ts";

const app = createApp(App)
app.use(router)
app.use(pinia)
app.use(Notify)
app.use(Icon)
app.use(Popover)
app.use(ImagePreview)
app.mount('#app')

// 建立sse连接
const sseStore = useSseStore()
sseStore.buildSseConnection()

// // 当页面关闭或者刷新时，取消sse订阅
// document.addEventListener("visibilitychange", function logData() {
//     if (document.visibilityState === "hidden") {
//         const userInfo: UserInfo = JSON.parse(localStorage.getItem('userInfo') || '{}') as UserInfo
//         if (userInfo.id) {
//             // 创建一个formData
//             const formData: FormData = new FormData()
//             formData.append('userId', userInfo.id.toString())
//             navigator.sendBeacon(import.meta.env.VITE_API_PREFIX + '/sse/unsubscribe', formData)
//         }
//     }
// })

// 全局监听页面刷新
window.addEventListener('beforeunload', () => {
    // const sseStore = useSseStore()
    // sseStore.removeSseConnection()
    const userInfo: UserInfo = JSON.parse(localStorage.getItem('userInfo') || '{}') as UserInfo
    if (userInfo.id) {
        // 创建一个formData
        const formData: FormData = new FormData()
        formData.append('userId', userInfo.id.toString())
        navigator.sendBeacon(import.meta.env.VITE_API_PREFIX + '/sse/unsubscribe', formData)
    }
})
