import {defineStore} from "pinia";
import {RoomState} from "@/pinia/modules/room/type.ts";
import type {RoomList} from "@/api/message/type.ts";
import {RoomListResponse} from "@/api/message/type.ts";
import {reqRoomList} from "@/api/message";
import {showNotify} from "vant";

const useRoomStore = defineStore('Room', {
    state: (): RoomState => {
        return {
            roomList: [] as RoomList,
            unReadMsgCount: 0
        }
    },
    getters: {

    },
    actions: {
        async getRoomList(): Promise<void> {
            const resp: RoomListResponse = await reqRoomList()
            if (resp.statusCode != 0) {
                showNotify({type: 'danger', message: resp.statusMsg})
                return
            }
            this.roomList = resp.data
            this.getUnreadMsgCount()
        },
        getUnreadMsgCount(): void {
            // 得到未读消息数量
            this.unReadMsgCount = 0
            for (let i: number = 0; i < this.roomList.length; i++) {
                this.unReadMsgCount += this.roomList[i].unreadCount
            }
        }
    },
    persist: {
        // 修改存储中使用的键名称，默认为当前 Store的 id
        paths: ['unReadMsgCount'],
    }
})

export default useRoomStore