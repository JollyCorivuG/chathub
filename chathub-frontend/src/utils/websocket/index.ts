import type {OthersMsg, WSReq, WSRes} from "@/utils/websocket/type.ts";
import {WSReqType, WSResType} from "@/utils/websocket/type.ts";
import {showNotify} from "vant";

export class WS {
    private wsClient: WebSocket // websocket客户端
    private heartbeatTimer: NodeJS.Timeout | null = null
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
            console.log('收到他人消息')
            const msg: WSRes<OthersMsg> = JSON.parse(event.data as string) as WSRes<OthersMsg>
            // TODO 后续将这个放进消息队列中
            console.log(msg)
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
}