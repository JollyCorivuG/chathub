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
            redirect: '/home/message',
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
            path: '/user_page',
            component: () => import('@/views/user_page/index.vue'),
        },
        {
            path: '/friend_chat',
            component: () => import('@/views/friend_chat/index.vue'),
        },
        {
            path: '/group_chat',
            component: () => import('@/views/group_chat/index.vue'),
        },
        {
            path: '/create_talk',
            component: () => import('@/views/create_talk/index.vue'),
        },
        {
            path: '/info',
            component: () => import('@/views/info/index.vue'),
        },
        {
            path: '/change_info',
            component: () => import('@/views/change_info/index.vue'),
        },
        {
            path: '/create_group',
            component: () => import('@/views/create_group/index.vue'),
        },
        {
            path: '/group_page',
            component: () => import('@/views/group_page/index.vue'),
        },
        {
            path: '/404',
            component: () => import('@/views/404/index.vue')
        }
    ]
})