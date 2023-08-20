import { defineStore } from 'pinia'
import {MsgState} from "@/pinia/modules/message/type.ts";
import {MessageListResponse, ShowMsg} from "@/api/message/type.ts";
import {reqMessageList} from "@/api/message";
import {showNotify} from "vant";


const useMsgStore = defineStore('Msg', {
    state: (): MsgState => {
        return {
            msgList: [] as ShowMsg[],
            roomId: 0,
            isLastPage: false,
            cursor: '',
            isReceiveMsg: false,
        }
    },
    getters: {

    },
    actions: {
        async getMsgList(): Promise<number> {
            if (this.isLastPage) {
                return 0
            }
            const resp: MessageListResponse = await reqMessageList(this.roomId, this.cursor)
            if (resp.statusCode != 0) {
                showNotify({type: 'danger', message: resp.statusMsg})
                return 0
            }
            this.msgList.unshift(...resp.data.list.reverse())
            this.cursor = resp.data.cursor
            this.isLastPage = resp.data.isLast
            return resp.data.list.length
        },
        initData() {
            this.msgList = []
            this.isLastPage = false
            this.cursor = ''
        }
    }
})

export default useMsgStore