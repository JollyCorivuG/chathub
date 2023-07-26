import {AddFriendParams, CommonResponse, HandleFriendRequestParams} from "@/api/friend/type.ts";
import request from "@/utils/request";

enum FriendApi {
    // 发送好友申请
    sendFriendApplyUrl = '/friends/application',
    // 接受或拒绝好友申请
    friendApplyReplyUrl = '/friends/application/reply'
}

// 发送好友申请
export const reqSendFriendApply = (addFriendParams: AddFriendParams) => request.post<any, CommonResponse>(FriendApi.sendFriendApplyUrl, addFriendParams)

// 接受或拒绝好友申请
export const reqFriendApplyReply = (replyParams: HandleFriendRequestParams) => request.post<any, CommonResponse>(FriendApi.friendApplyReplyUrl, replyParams)