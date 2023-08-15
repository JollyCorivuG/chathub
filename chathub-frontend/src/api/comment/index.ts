import request from "@/utils/request.ts";
import {PostCommentParams, PostCommentResponse} from "@/api/comment/type.ts";

enum CommentApi {
    // 发布评论
    postCommentUrl = '/comments/post',
}

// 发布评论
export const reqPostComment = (params: PostCommentParams) => request.post<any, PostCommentResponse>(CommentApi.postCommentUrl, params)