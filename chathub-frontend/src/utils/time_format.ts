export const convertJavaTimeToString = (jTime: string): string => {
    return jTime.replace('T', ' ')
}