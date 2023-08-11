export const generateFileIcon = (fileName: string) => {
    // 根据文件名, 生成文件图标的url, 统一格式为/src/assets/images/file/文件后缀名.png
    const fileSuffix = fileName.split('.').pop()?.toUpperCase()
    return (import.meta as any).env.VITE_STATIC_ASSETS_PATH + `images/file/${fileSuffix}.png`
}

export const calcMB = (size: number) => {
    // size是以字节为单位的文件大小, 计算出以MB为单位的文件大小
    return (size / 1024 / 1024).toFixed(2)
}