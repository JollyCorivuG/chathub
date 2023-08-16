import request from "@/utils/request.ts";
import {CreateTalkParams, CreateTalkResponse, LikeParams, TalkListResponse} from "@/api/trend/type.ts";
import {CommonResponse} from "@/api/user/type.ts";

enum TrendApi {
    // 发布说说
    createTalkUrl = '/trend/talk',
    // 分页获取消息列表
    talkListUrl = '/trend/talk/list',
    // 点赞
    likeUrl = '/trend/talk/like',
}

// 发布说说
export const reqCreateTalk = (params: CreateTalkParams) => request.post<any, CreateTalkResponse>(TrendApi.createTalkUrl, params)

// 分页获取消息列表
export const reqTalkList = (pageSize: number, cursor: string) => request.get<any, TalkListResponse>(TrendApi.talkListUrl + '?' + `cursor=${cursor}` + '&' + `pageSize=${pageSize}`)

// 点赞
export const reqLike = (params: LikeParams) => request.post<any, CommonResponse>(TrendApi.likeUrl, params)