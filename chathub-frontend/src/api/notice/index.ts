import request from "@/utils/request";
import {FriendNoticeListResponse, CommonResponse, GroupNoticeListResponse} from "@/api/notice/type.ts";

enum NoticeApi {
    // 查询好友通知
    friendNoticeListUrl = '/notices/friends',
    // 删除好友通知
    deleteFriendNoticeUrl = '/notices/friends',
    // 查询群组通知
    groupNoticeListUrl = '/notices/groups',
    // 删除群组通知
    deleteGroupNoticeUrl = '/notices/groups',
}

// 查询好友通知
export const reqFriendNoticeList = () => request.get<any, FriendNoticeListResponse>(NoticeApi.friendNoticeListUrl)

// 删除好友通知
export const reqDeleteFriendNotice = (noticeId: number) => request.delete<any, CommonResponse>(NoticeApi.deleteFriendNoticeUrl + `/${noticeId}`)

// 查询群组通知
export const reqGroupNoticeList = () => request.get<any, GroupNoticeListResponse>(NoticeApi.groupNoticeListUrl)

// 删除群组通知
export const reqDeleteGroupNotice = (noticeId: number) => request.delete<any, CommonResponse>(NoticeApi.deleteGroupNoticeUrl + `/${noticeId}`)

