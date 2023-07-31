const baseUrl: string = 'ws://localhost:8090'
export const buildWSUrl = (token: string, roomId: number): string => {
    return `${baseUrl}?token=${token}&roomId=${roomId}`
}