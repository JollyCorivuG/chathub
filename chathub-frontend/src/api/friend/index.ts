import {AddFriendParams, CommonResponse, FriendListResponse, HandleFriendRequestParams} from "@/api/friend/type.ts";
import request from "@/utils/request";

enum FriendApi {
    // 发送好友申请
    sendFriendApplyUrl = '/friends/application',
    // 接受或拒绝好友申请
    friendApplyReplyUrl = '/friends/application/reply',
    // 获取好友列表
    friendListUrl = '/friends/list',
    // 删除好友
    deleteFriendUrl = '/friends'
}

// 发送好友申请
export const reqSendFriendApply = (addFriendParams: AddFriendParams) => request.post<any, CommonResponse>(FriendApi.sendFriendApplyUrl, addFriendParams)

// 接受或拒绝好友申请
export const reqFriendApplyReply = (replyParams: HandleFriendRequestParams) => request.post<any, CommonResponse>(FriendApi.friendApplyReplyUrl, replyParams)

// 获取好友列表
export const reqFriendList = () => request.get<any, FriendListResponse>(FriendApi.friendListUrl)

// 删除好友
export const reqDeleteFriend = (friendId: number) => request.delete<any, CommonResponse>(FriendApi.deleteFriendUrl + `/${friendId}`)