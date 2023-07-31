import type {SendMsg} from "@/api/message/type.ts";
import request from "@/utils/request.ts";
import {SendMsgResponse} from "@/api/message/type.ts";

enum MessageApi {
    // 发送消息
    sendMsgUrl = '/messages/send'
}

// 发送消息
export const reqSendMsg = (sendMsg: SendMsg) => request.post<any, SendMsgResponse>(MessageApi.sendMsgUrl, sendMsg)

