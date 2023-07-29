import { createRouter, createWebHistory } from 'vue-router'

export default createRouter({
    // 路由配置
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/login',
            component: () => import('@/views/login/index.vue')
        },
        {
            path: '/register',
            component: () => import('@/views/register/index.vue')
        },
        {
            path: '/home',
            component: () => import('@/views/home/index.vue'),
            children: [
                {
                    path: 'message',
                    component: () => import('@/views/home/message/index.vue'),
                },
                {
                    path: 'contact',
                    component: () => import('@/views/home/contact/index.vue'),
                },
                {
                    path: 'notice',
                    component: () => import('@/views/home/notice/index.vue'),
                },
                {
                    path: 'trend',
                    component: () => import('@/views/home/trend/index.vue'),
                }
            ]
        },
        {
            path: '/add',
            component: () => import('@/views/add/index.vue'),
            children: [
                {
                    path: 'friend',
                    component: () => import('@/views/add/add_friend/index.vue'),
                },
                {
                    path: 'group',
                    component: () => import('@/views/add/add_group/index.vue'),
                }
            ]
        },
        {
            path: '/404',
            component: () => import('@/views/404/index.vue')
        }
    ],
    //滚动行为:控制滚动条的位置
    scrollBehavior() {
        return {
            left: 0,
            top: 0
        }
    }
})