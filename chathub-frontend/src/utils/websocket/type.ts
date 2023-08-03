export enum WSReqType {
    HEARTBEAT = 0, // 心跳检验
    AUTHORIZE = 1 // 握手认真
}

// 向ws服务器发送的消息类型
export interface WSReq {
    type: number,
    data?: string
}

export enum WSResType {
    NORMAL_MSG = 0, // 普通消息
    HEAD_SHAKE_SUCCESS_MSG = 1, // 握手成功消息
    WS_HEAD_SHAKE_FAIL_MSG = 2, // 握手失败消息
}

// 从ws服务器接收的消息类型
export interface WSRes<T> {
    type: number,
    data?: T
}

// 从ws接收的他人发送的消息类型
export interface OthersMsg {
    fromUser: {
        id: number,
    }
    message: {
        id: number,
        sendTime: number,
        msgType: number,
        body: object
    }
}