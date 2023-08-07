const baseUrl: string = import.meta.env.VITE_WS_URL as string
export const buildWSUrl = (token: string, roomId: number): string => {
    return `${baseUrl}?token=${token}&roomId=${roomId}`
}