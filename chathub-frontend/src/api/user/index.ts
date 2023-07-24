import request from '@/utils/request'
import type {
    CommonResponse,
    LoginByPhoneParams,
    LoginParams,
    LoginResponse,
    RegisterParams,
    RegisterResponse
} from './type'
import {GetUserInfoResponse} from "./type";

enum UserApi {
    // 登录
    loginUrl = '/users/login',
    // 手机验证码登录
    loginByPhoneUrl = '/users/phone_login',
    // 获取验证码
    getPhoneCodeUrl = '/users/phone_code',
    // 注册
    registerUrl = '/users/register',
    // 登录后获取自己的信息
    getSelfInfoUrl = '/users/info/me',
    // 登出
    logoutUrl = '/users/logout'
}

// 登录
export const reqLogin = (loginForm: LoginParams) => request.post<any, LoginResponse>(UserApi.loginUrl, loginForm)

// 手机验证码登录
export const reqLoginByPhone = (loginForm: LoginByPhoneParams) => request.post<any, LoginResponse>(UserApi.loginByPhoneUrl, loginForm)

// 获取验证码
export const reqPhoneCode = (phone: string) => request.get<any, CommonResponse>(UserApi.getPhoneCodeUrl + `/${phone}`)

// 注册
export const reqRegister = (registerForm: RegisterParams) => request.post<any, RegisterResponse>(UserApi.registerUrl, registerForm)

// 登录后获取自己的信息
export const reqGetSelfInfo = () => request.get<any, GetUserInfoResponse>(UserApi.getSelfInfoUrl)

// 登出
export const reqLogout = () => request.get<any, CommonResponse>(UserApi.logoutUrl)