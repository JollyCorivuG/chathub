import { defineStore } from 'pinia'
import {reqLogin, reqLoginByPhone} from '@/api/user'
import {CommonResponse, LoginByPhoneParams, LoginParams, LoginResponse} from "@/api/user/type.ts";

const useUserStore = defineStore('User', {
    state: () => {
        return {
        }
    },
    getters: {

    },
    actions: {
        async login(loginForm: LoginParams): Promise<CommonResponse> {
            const resp: LoginResponse = await reqLogin(loginForm)
            if (resp.statusCode == 0) {
                // TODO 登录成功的逻辑
            }
            return resp as CommonResponse
        },
        async loginByPhone(loginForm: LoginByPhoneParams): Promise<CommonResponse> {
            const resp: LoginResponse = await reqLoginByPhone(loginForm)
            if (resp.statusCode == 0) {
                // TODO 登录成功的逻辑
            }
            return resp as CommonResponse
        }
    }
})

export default useUserStore