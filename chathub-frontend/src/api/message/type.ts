// 通用响应信息
import {UserInfo} from "@/api/user/type.ts";
import {GroupInfo} from "@/api/group/type.ts";

export interface CommonResponse {
    statusCode: number,
    statusMsg: string,
}

// 发送消息的请求类型
export interface SendMsg {
    roomId: number,
    msgType: number,
    body: object | any // 具体的消息体, 根据不同的消息类型, 传入不同的消息体
}

export enum MsgType {
    TEXT = 0, // 文本消息
    IMG = 1, // 图片消息
    FILE = 2 // 文件消息
}

// 文本消息
export interface TextMsg {
    content: string
}

// 图片消息
export interface ImgMsg {
    size: number,
    width: number,
    height: number,
    url: string
}

// 文件消息
export interface FileMsg {
    size: number,
    url: string,
    fileName: string
}

export interface MsgBody {
    content?: string,
    imgMsg?: ImgMsg,
    fileMsg?: FileMsg
}

// 展示消息的类型
export interface ShowMsg {
    fromUser: {
        id: number,
    }
    message: {
        id: number,
        sendTime: string,
        msgType: number,
        body: MsgBody
    }
}

// 发送消息的响应类型
export interface SendMsgResponse extends CommonResponse {
    data: ShowMsg
}

// 获取消息列表的请求类型
export interface MessageListResponse extends CommonResponse {
    data: {
        list: ShowMsg[],
        cursor: string
        isLast: boolean
    }
}

// 会话类型
export interface Room {
    id: number,
    roomType: number,
    connectInfo: UserInfo | GroupInfo,
    latestMsg: ShowMsg,
    unreadCount: number
}

export type RoomList = Room[]

// 获取会话列表的响应类型
export interface RoomListResponse extends CommonResponse {
    data: RoomList
}


