import {UserInfo} from "@/api/user/type.ts";
import {ShowCommentInfo} from "@/api/comment/type.ts";

// 通用响应信息
export interface CommonResponse {
    statusCode: number,
    statusMsg: string,
}

// 类型枚举
export enum ExtraType {
    IMG = 0,
    VIDEO = 1
}

// 图片和视频
export interface ImageInfo {
    url: string;
}
export interface VideoInfo {
    url: string;
    coverUrl: string;
}

// 发布说说的额外信息, 如图片, 视频等
export interface ExtraInfo {
    type: number;
    data: ImageInfo | VideoInfo;
}

// 展示的说说信息
export interface ShowTalkInfo {
    id: number;
    author: UserInfo
    content: string;
    extra: ExtraInfo[];
    likeCount: number;
    isLike: boolean;
    latestLikeUsers: UserInfo[];
    comments: ShowCommentInfo[];

    createTime: string;
}

// 发布说说的参数
export interface CreateTalkParams {
    content: string;
    extra: ExtraInfo[];
}

// 发布说说的响应信息
export interface CreateTalkResponse extends CommonResponse {
    data: ShowTalkInfo;
}

// 分页获取说说列表的响应信息
export interface TalkListResponse extends CommonResponse {
    data: {
        list: ShowTalkInfo[];
        cursor: string;
        isLast: boolean;
    }
}

// 点赞的请求参数
export interface LikeParams {
    talkId: number;
    isLike: boolean;
}

