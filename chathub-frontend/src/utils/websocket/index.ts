import type {OthersMsg, WSReq, WSRes} from "@/utils/websocket/type.ts";
import {WSReqType, WSResType} from "@/utils/websocket/type.ts";
import {showNotify} from "vant";
import useMsgStore from "@/pinia/modules/message";
import {convertTimeStampToJavaTime} from "@/utils/time_format.ts";
import {MsgBody, ShowMsg} from "@/api/message/type.ts";

const msgStore = useMsgStore()

export class WS {
    private wsClient: WebSocket // websocket客户端
    private heartbeatTimer: number = 0 // 心跳检测定时器
    constructor(url: string) {
        this.wsClient = new WebSocket(url)
        // 收到消息
        this.wsClient.addEventListener('message', this.onReceiveMsg)
        // 建立链接
        this.wsClient.addEventListener('open', this.onConnectOpen)
        // 关闭连接
        this.wsClient.addEventListener('close', this.onConnectClose)
        // 连接错误
        this.wsClient.addEventListener('error', this.onConnectError)
    }

    // 收到消息
    private onReceiveMsg = (event: MessageEvent): void => {
        const msg: any = JSON.parse(event.data as string)
        if (msg.type == WSResType.WS_HEAD_SHAKE_FAIL_MSG) {
            showNotify({
                type: 'danger',
                message: 'websocket握手失败',
                duration: 1000,
            })
        } else if (msg.type == WSResType.HEAD_SHAKE_SUCCESS_MSG) {
            // 在握手成功后, 开启定时器, 每隔10s向服务器发送一次心跳检测
            this.heartbeatTimer = setInterval(() => {
                this.sendMsg({type: WSReqType.HEARTBEAT})
            }, 10000)
        } else if (msg.type == WSResType.NORMAL_MSG) {
            const msg: WSRes<OthersMsg> = JSON.parse(event.data as string) as WSRes<OthersMsg>
            console.log(msg)
            const showMsg: ShowMsg = {
                fromUser: {
                    id: msg.data?.fromUser.id as number,
                },
                message: {
                    id: msg.data?.message.id as number,
                    sendTime: convertTimeStampToJavaTime(<number>msg.data?.message.sendTime),
                    msgType: msg.data?.message.msgType as number,
                    body: msg.data?.message.body as MsgBody
                }
            }
            msgStore.msgList.push(showMsg)
            msgStore.isReceiveMsg = !msgStore.isReceiveMsg

        }
    }

    // 建立连接
    private onConnectOpen = (event: Event): void => {
        console.log('建立连接' + event)
    }

    // 关闭连接
    private onConnectClose = (event: CloseEvent): void=>  {
        console.log('关闭连接' + event)
        if (this.heartbeatTimer) {
            clearInterval(this.heartbeatTimer)
        }
    }

    // 连接错误
    private onConnectError = (event: Event): void => {
        console.log('连接错误' + event)
    }

    // 发送消息 (暴露给外部的方法)
    public sendMsg = (msg: WSReq): void => {
        if (this.wsClient.readyState === 1) {
            this.wsClient.send(JSON.stringify(msg))
        }
    }

    // 断开连接 (暴露给外部的方法)
    public closeConnect = (): void => {
        this.wsClient.close()
    }
}