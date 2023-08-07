// 对axios库的二次封装
import axios from 'axios'
import { showNotify } from 'vant'
import router from '@/router'
import useUserStore from "@/pinia/modules/user";

// 创建一个axios实例: 可以设置基础路径, 超时时间等
const request = axios.create({
    baseURL: import.meta.env.VITE_API_PREFIX as string,
    timeout: 30000
})

// 请求拦截器
request.interceptors.request.use((config) => {
    // config: 请求拦截器回调注入的对象(配置对象), 配置对象有headers属性
    const userStore = useUserStore()
    if (userStore.token) {
        config.headers.token = userStore.token
    }
    return config
})

// 响应拦截器
request.interceptors.response.use((response) => {
    // response: 响应拦截器回调注入的对象(响应对象), 响应对象有data属性
    return response.data
}, (error) => {
    // 处理http错误状态码
    const status = error.response.status
    switch (status) {
        case 404:
            //错误提示信息
            showNotify({
                type: 'danger',
                message: '请求路径出现问题',
                duration: 1000,
            })
            break;
        case 500 | 501 | 502 | 503 | 504 | 505:
            showNotify({
                type: 'danger',
                message: '服务器出错',
                duration: 1000,
            })
            break;
        case 401:
            showNotify({
                type: 'danger',
                message: 'token过期, 获取会话列表失败',
                duration: 1000,
            })
            // 直接返回登录页面, 清除localStorage中数据
            localStorage.clear()
            router.push({path: '/login'}).then(r => r)
            break;
    }
    return Promise.reject(new Error(error.message))
})

// 导出
export default request