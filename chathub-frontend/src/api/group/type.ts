import type {UserInfo} from "@/api/user/type.ts";

export interface CommonResponse {
    statusCode: number,
    statusMsg: string
}

export interface GroupInfo {
    id: number,
    number: string,
    name: string,
    avatar: string,
    peopleNum: number,
    owner?: UserInfo,
    members?: UserInfo[],
    roomId: number,
    createTime: string
}

// 创建群组请求参数
export interface CreateGroupParams {
    name: string,
    avatar: string,
    maxPeopleNum: number,
}

// 创建群组响应信息
export interface CreateGroupResponse extends CommonResponse {
    data: GroupInfo
}

// 群组列表响应信息
export interface GroupListResponse extends CommonResponse {
    data: GroupInfo[]
}

// 群组信息响应信息
export interface GroupInfoResponse extends CommonResponse {
    data: GroupInfo
}

// 响应群组邀请的请求参数
export interface InvitationRespParams {
    groupId: number,
    noticeId: number,
    isAgree: boolean
}






