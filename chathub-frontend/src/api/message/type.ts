// 通用响应信息
export interface CommonResponse {
    statusCode: number,
    statusMsg: string,
}

// 发送消息的请求类型
export interface SendMsg {
    roomId: number,
    msgType: number,
    body: object // 具体的消息体, 根据不同的消息类型, 传入不同的消息体
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

// 展示消息的类型
export interface ShowMsg {
    fromUserInfo: {
        id: number,
    }
    message: {
        id: number,
        sendTime: string,
        msgType: number,
        body: object
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


