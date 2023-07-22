import { createApp } from 'vue'
import App from './App.vue'
import '@/styles/reset.scss'
import router from '@/router'
import pinia from '@/pinia'

const app = createApp(App)
app.use(router)
app.use(pinia)
app.mount('#app')
