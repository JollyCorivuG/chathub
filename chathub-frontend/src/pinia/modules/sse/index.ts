import { defineStore } from 'pinia'
import type {SseState} from "@/pinia/modules/sse/type.ts";
import {showNotify} from "vant";
import router from "@/router";
import useRoomStore from "@/pinia/modules/room";


const useSseStore = defineStore('Sse', {
    state: (): SseState => {
        return {
            eventSource: {} as any
        }
    },
    getters: {

    },
    actions: {
        buildSseConnection(): void {
            const token: string = localStorage.getItem('token') as string || ''
            this.eventSource = new EventSource(import.meta.env.VITE_API_PREFIX + '/sse/subscribe?' + 'token=' + token, {
                withCredentials: true
            })
            this.eventSource.onmessage = (event: MessageEvent) => {
                const msg = JSON.parse(event.data) as any
                console.log(msg)
                if (msg.type == 1) {
                    // 说明是强制下线消息
                    showNotify({type: 'danger', message: '您的账号在其他地方登录，您已被强制下线'})
                    this.eventSource.close()
                    localStorage.clear()
                    router.push({path: '/login'}).then(r => r)
                } else if (msg.type == 0) {
                    // 说明是刷新房间列表的消息
                    const roomStore = useRoomStore()
                    roomStore.roomList = msg.data
                    roomStore.getUnreadMsgCount()
                }
            }
        },
        removeSseConnection(): void {
            fetch(import.meta.env.VITE_API_PREFIX + '/sse/close', {
                method: 'GET',
                headers: {
                    'token': localStorage.getItem('token') || ''
                }
            }).then(r => console.log(r))
            this.eventSource.close()
        }
    }
})


export default useSseStore