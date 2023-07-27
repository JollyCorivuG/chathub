// 通用响应信息
import {UserList} from "@/api/user/type.ts";

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

// 好友列表响应信息
export interface FriendListResponse extends CommonResponse {
    data: UserList
}