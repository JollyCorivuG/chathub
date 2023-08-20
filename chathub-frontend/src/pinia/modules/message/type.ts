import {ShowMsg} from "@/api/message/type.ts";

export interface MsgState {
    msgList: ShowMsg[],
    roomId: number
    isLastPage: boolean,
    cursor: string,
    isReceiveMsg: boolean
}