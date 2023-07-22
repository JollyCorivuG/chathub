import request from '@/utils/request'
import type {CommonResponse, LoginByPhoneParams, LoginParams, LoginResponse} from './type'

enum UserApi {
    // 登录
    loginUrl = '/users/login',
    // 手机验证码登录
    loginByPhoneUrl = '/users/phone_login',
    // 获取验证码
    getPhoneCodeUrl = '/users/phone_code',
}

// 登录
export const reqLogin = (loginForm: LoginParams) => request.post<any, LoginResponse>(UserApi.loginUrl, loginForm)

// 手机验证码登录
export const reqLoginByPhone = (loginForm: LoginByPhoneParams) => request.post<any, LoginResponse>(UserApi.loginByPhoneUrl, loginForm)

// 获取验证码
export const reqPhoneCode = (phone: string) => request.get<any, CommonResponse>(UserApi.getPhoneCodeUrl + `/${phone}`)