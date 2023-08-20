import {RoomList} from "@/api/message/type.ts";

export interface RoomState {
    roomList: RoomList,
    unReadMsgCount: number
}