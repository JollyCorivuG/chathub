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

// 登录响应信息
export interface LoginResponse extends CommonResponse {
    data: string
}