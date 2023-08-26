import request from "@/utils/request.ts";
import {
    CommonResponse,
    CreateGroupParams,
    CreateGroupResponse,
    GroupInfoResponse,
    GroupListResponse, InvitationRespParams
} from "@/api/group/type.ts";

enum GroupApi {
    // 创建群组
    createGroupUrl = '/groups/create',
    // 获取自己管理的群组
    myManageGroupUrl = '/groups/manage',
    // 获取自己加入的群组
    myJoinGroupUrl = '/groups/join',
    // 根据 id 获取群组信息
    groupInfoUrl = '/groups',
    // 邀请好友加入群组
    inviteUrl = '/groups',
    // 响应群组邀请
    respInvitation = '/groups/resp'
}

// 创建群组
export const reqCreateGroup = (params: CreateGroupParams) => request.post<any, CreateGroupResponse>(GroupApi.createGroupUrl, params)

// 获取自己管理的群组
export const reqMyManageGroup = () => request.get<any, GroupListResponse>(GroupApi.myManageGroupUrl)

// 获取自己加入的群组
export const reqMyJoinGroup = () => request.get<any, GroupListResponse>(GroupApi.myJoinGroupUrl)

// 根据 id 获取群组信息
export const reqGroupInfo = (id: number) => request.get<any, GroupInfoResponse>(`${GroupApi.groupInfoUrl}/${id}`)

// 邀请好友加入群组
export const reqInviteFriend = (groupId: number, ids: number[]) => request.post<any, CommonResponse>(GroupApi.inviteUrl + '/' + groupId + '/invite', ids)

// 响应群组邀请
export const reqInvitationResp = (params: InvitationRespParams) => request.post<any, CommonResponse>(GroupApi.respInvitation, params)
