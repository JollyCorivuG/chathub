export const convertJavaTimeToString = (jTime: string): string => {
    return jTime.replace('T', ' ')
}

export const convertJavaTimeToTimeStamp = (jTime: string): number => {
    return Date.parse(jTime)
}