import request from '@/utils/request'
import type {
    CommonResponse,
    LoginByPhoneParams,
    LoginParams,
    LoginResponse,
    RegisterParams,
    RegisterResponse, UserInfo
} from './type'
import {GetUserInfoResponse, SearchUserResponse} from "./type";

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
    logoutUrl = '/users/logout',
    // 根据关键字查询用户
    searchUserUrl = '/users/query',
    // 根据用户id查询用户信息
    getUserInfoByIdUrl = '/users/info',
    // 更新用户信息
    updateUserInfoUrl = '/users/info/me',
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

// 根据关键字查询用户
export const reqSearchUser = (keyword: string, currentPage: number) => request.get<any, SearchUserResponse>(UserApi.searchUserUrl + `/${keyword}` + '?page=' + currentPage)

// 根据用户id查询用户信息
export const reqGetUserInfoById = (userId: number) => request.get<any, GetUserInfoResponse>(UserApi.getUserInfoByIdUrl + `/${userId}`)

// 更新用户信息
export const reqUpdateUserInfo = (userInfo: UserInfo) => request.put<any, GetUserInfoResponse>(UserApi.updateUserInfoUrl, userInfo)