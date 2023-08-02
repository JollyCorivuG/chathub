import type {SendMsg} from "@/api/message/type.ts";
import request from "@/utils/request.ts";
import {SendMsgResponse, MessageListResponse} from "@/api/message/type.ts";

enum MessageApi {
    // 发送消息
    sendMsgUrl = '/messages/send',
    // 获取消息列表
    messageListUrl = '/messages/list',
}

// 发送消息
export const reqSendMsg = (sendMsg: SendMsg) => request.post<any, SendMsgResponse>(MessageApi.sendMsgUrl, sendMsg)

// 获取消息列表
export const reqMessageList = (roomId: number, cursor: string) => request.get<any, MessageListResponse>(MessageApi.messageListUrl + '?' + `roomId=${roomId}&cursor=${cursor}`)


