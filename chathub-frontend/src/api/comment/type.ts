// 通用响应信息
import {UserInfo} from "@/api/user/type.ts";

export interface CommonResponse {
    statusCode: number,
    statusMsg: string,
}

// 展示的评论信息
export interface ShowCommentInfo {
    id: number;
    sender: UserInfo;
    content: string;
    replyUserId: number;
    replyUser?: UserInfo;
    createTime: string;
}

// 发布评论的参数
export interface PostCommentParams {
    talkId: number;
    content: string;
    replyUserId: number;
}

// 发布评论的响应信息
export interface PostCommentResponse extends CommonResponse {
    data: ShowCommentInfo;
}


