// 通用响应信息
export interface CommonResponse {
    statusCode: number,
    statusMsg: string,
}

// 上传文件响应信息
export interface UploadFileResponse extends CommonResponse {
    data: string
}