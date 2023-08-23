import { defineStore } from 'pinia'
import {reqGetSelfInfo, reqLogin, reqLoginByPhone, reqLogout, reqRegister, reqUpdateUserInfo} from '@/api/user'
import {
    CommonResponse, GetUserInfoResponse,
    LoginByPhoneParams,
    LoginParams,
    LoginResponse,
    RegisterParams,
    RegisterResponse, UserInfo
} from "@/api/user/type.ts";
import {UserState} from "@/pinia/modules/user/type.ts";
import useSseStore from "@/pinia/modules/sse";

const useUserStore = defineStore('User', {
    state: (): UserState => {
        return {
            token: localStorage.getItem('token') || '',
            userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}') as UserInfo
        }
    },
    getters: {

    },
    actions: {
        async afterLogin(token: string) {
            // 1.将token保存到localStorage中
            localStorage.setItem('token', token)
            this.token = token

            // 2.向后端获取请求得到个人信息
            const resp: GetUserInfoResponse = await reqGetSelfInfo()

            // 3.将个人信息保存到localStorage中
            localStorage.setItem('userInfo', JSON.stringify(resp.data))
            this.userInfo = resp.data

            // 4.建立sse连接
            const sseStore = useSseStore()
            sseStore.buildSseConnection()
        },
        async login(loginForm: LoginParams): Promise<CommonResponse> {
            const resp: LoginResponse = await reqLogin(loginForm)
            if (resp.statusCode == 0) {
                await this.afterLogin(resp.data)
            }
            return resp as CommonResponse
        },
        async loginByPhone(loginForm: LoginByPhoneParams): Promise<CommonResponse> {
            const resp: LoginResponse = await reqLoginByPhone(loginForm)
            if (resp.statusCode == 0) {
                await this.afterLogin(resp.data)
            }
            return resp as CommonResponse
        },
        async register(registerForm: RegisterParams): Promise<CommonResponse> {
            const resp: RegisterResponse = await reqRegister(registerForm)
            if (resp.statusCode == 0) {
                await this.afterLogin(resp.data)
            }
            return resp as CommonResponse
        },
        async logout(): Promise<CommonResponse> {
            const sseStore = useSseStore()
            sseStore.removeSseConnection()
            const resp: CommonResponse = await reqLogout()
            localStorage.clear()
            return resp as CommonResponse
        },
        async updateUserInfo(userInfo: UserInfo): Promise<CommonResponse> {
            const resp: GetUserInfoResponse = await reqUpdateUserInfo(userInfo)
            localStorage.setItem('userInfo', JSON.stringify(resp.data))
            this.userInfo = resp.data
            return resp as CommonResponse
        }
    }
})

export default useUserStore