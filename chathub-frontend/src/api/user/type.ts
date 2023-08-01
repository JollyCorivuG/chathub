// 通用响应信息
export interface CommonResponse {
    statusCode: number,
    statusMsg: string,
}

// 账号密码登录请求参数
export interface LoginParams {
    account: string,
    password: string
}

// 手机验证码登录请求参数
export interface LoginByPhoneParams {
    phone: string,
    code: string
}

// 注册请求参数
export interface RegisterParams {
    phone: string,
    account: string,
    password: string
}

// 登录响应信息
export interface LoginResponse extends CommonResponse {
    data: string
}

// 注册响应信息
export interface RegisterResponse extends CommonResponse {
    data: string
}

// 用户信息
export interface UserInfo {
    id: number,
    phone: string,
    account: string,
    nickName: string,
    avatarUrl: string,
    level: number,
    friendCount: number,
    groupCount: number,
    isOnline: boolean,
    isFriend: boolean,
    becomeFriendTime: string,
    roomId: number,
}

// 获取用户信息响应信息
export interface GetUserInfoResponse extends CommonResponse {
    data: UserInfo
}

// 根据关键字查询用户响应信息
export type UserList = UserInfo[]
export interface SearchUserResponse extends CommonResponse {
    data: UserList
}
