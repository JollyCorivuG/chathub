// 通用响应信息
import {GroupInfo} from "@/api/group/type.ts";

export interface CommonResponse {
    statusCode: number,
    statusMsg: string,
}

// 好友通知响应信息
export interface ShowUserInfo {
    id: number,
    phone: string,
    account: string,
    nickName: string,
    avatarUrl: string,
    level: number,
    friendCount: number,
    groupCount: number,
}
export interface FriendNotice {
    id: number,
    showUserInfo: ShowUserInfo,
    description: string,
    noticeType: number,
    statusInfo: number,
    createTime: string,
}
export type FriendNoticeList = FriendNotice[]
export interface FriendNoticeListResponse extends CommonResponse {
    data: FriendNoticeList
}

// 群组通知响应信息
export interface GroupNotice {
    id: number,
    showGroupInfo: GroupInfo,
    description: string,
    statusInfo: number,
    createTime: string
}

export interface GroupNoticeListResponse extends CommonResponse {
    data: GroupNotice[]
}


