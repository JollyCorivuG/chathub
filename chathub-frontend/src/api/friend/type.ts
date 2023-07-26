// 通用响应信息
export interface CommonResponse {
    statusCode: number,
    statusMsg: string
}

// 添加好友请求参数
export interface AddFriendParams {
    toUserId: number,
    description: string
}

// 接收或拒绝好友请求参数
export interface HandleFriendRequestParams {
    noticeId: number,
    isAccept: boolean
    userId: number
}