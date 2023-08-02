import request from "@/utils/request";
import {UploadFileResponse} from "@/api/upload/type.ts";
enum uploadApi {
    uploadUrl = '/oss/upload'
}

// 上传文件
export const reqUploadFile = (data: FormData) => request.post<any, UploadFileResponse>(uploadApi.uploadUrl, data)