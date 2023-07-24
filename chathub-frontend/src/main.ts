import { createApp } from 'vue'
import App from './App.vue'
import '@/styles/reset.scss'
import router from '@/router'
import pinia from '@/pinia'
import { Notify } from 'vant'
import 'vant/es/notify/style'
import { Icon } from 'vant';
import { Popover } from 'vant'

const app = createApp(App)
app.use(router)
app.use(pinia)
app.use(Notify)
app.use(Icon)
app.use(Popover)
app.mount('#app')
