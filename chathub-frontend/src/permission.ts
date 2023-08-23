// 添加路由守卫
import router from "@/router";
import {NavigationGuardNext, RouteLocationNormalized} from "vue-router";
import NProgress from "nprogress";
import 'nprogress/nprogress.css';
import {showNotify} from "vant";
import useUserStore from "@/pinia/modules/user";
NProgress.configure({showSpinner: false})

// 前置守卫
// @ts-ignore
router.beforeEach(async (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
    NProgress.start()
    // 如果是404页面, 则直接跳转
    if (to.path === '/404') {
        next()
        return
    }
    // 如果路径不在路由表中, 则跳转到404页面
    if (router.resolve(to.path).matched.length === 0) {
        next('/404')
        return
    }

    // 根据token进行路由鉴权
    const token = localStorage.getItem('token')
    if (to.path !== '/login' && to.path !== '/register' && to.path !== '/') {
        if (!token) {
            showNotify({type: 'danger', message: '请先登录!'})
            next('/login')
        } else {
            next()
        }
        return
    }
    // 如果已经登录, 则进行登出操作
    if (token) {
        if (to.path == '/login') {
            next('/home')
            return
        }

        // 如果是从其他页面跳转到登录页面, 则进行登出操作
        const userStore = useUserStore()
        await userStore.logout()
        showNotify({type: 'primary', message: '已退出登录!'})
    }

    next()
})

// 后置守卫
// @ts-ignore
router.afterEach((to: RouteLocationNormalized, from: RouteLocationNormalized) => {
    NProgress.done()
})