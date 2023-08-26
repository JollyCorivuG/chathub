export const convertJavaTimeToString = (jTime: string): string => {
    return jTime.replace('T', ' ')
}

export const convertJavaTimeToTimeStamp = (jTime: string): number => {
    return Date.parse(jTime)
}

export const convertJavaTimeToStringYearMonthDay = (jTime: string): string => {
    const date: Date = new Date(jTime)
    const year: number = date.getFullYear()
    const month: number = date.getMonth() + 1
    const day: number = date.getDate()
    const formatMonth: string = month < 10 ? `0${month}` : `${month}`
    const formatDay: string = day < 10 ? `0${day}` : `${day}`
    return `${year}年${formatMonth}月${formatDay}日`
}

export const convertTimeStampToJavaTime = (timeStamp: number): string => {
    // 将时间戳转换为时间格式为：2021-08-01T12:00:00不要后面的.000Z
    const date: Date = new Date(timeStamp)
    const year: number = date.getFullYear()
    const month: number = date.getMonth() + 1
    const day: number = date.getDate()
    const hour: number = date.getHours()
    const minute: number = date.getMinutes()
    const second: number = date.getSeconds()
    const formatMonth: string = month < 10 ? `0${month}` : `${month}`
    const formatDay: string = day < 10 ? `0${day}` : `${day}`
    const formatHour: string = hour < 10 ? `0${hour}` : `${hour}`
    const formatMinute: string = minute < 10 ? `0${minute}` : `${minute}`
    const formatSecond: string = second < 10 ? `0${second}` : `${second}`
    return `${year}-${formatMonth}-${formatDay}T${formatHour}:${formatMinute}:${formatSecond}`
}

// 比较两个时间是否是在同一分钟, 两个时间的格式为: 2021-08-01T12:00:00
export const isSameMinute = (time1: string, time2: string): boolean => {
    const time1Arr: string[] = time1.split(':')
    const time2Arr: string[] = time2.split(':')
    return time1Arr[0] === time2Arr[0] && time1Arr[1] === time2Arr[1];
}

// 格式化时间, 如果时间是当天就显示时分, 如果是昨天就显示昨天 时分, 如果是昨天之前就显示月日 时分
export const formatTime = (time: string): string => {
    const now: Date = new Date()
    const nowYear: number = now.getFullYear()
    const nowMonth: number = now.getMonth() + 1
    const nowDay: number = now.getDate()
    now.getHours();
    now.getMinutes();
    now.getSeconds();
    const timeArr: string[] = time.split('T')
    const dateArr: string[] = timeArr[0].split('-')
    const year: number = parseInt(dateArr[0])
    const month: number = parseInt(dateArr[1])
    const day: number = parseInt(dateArr[2])
    const hour: number = parseInt(timeArr[1].split(':')[0])
    const minute: number = parseInt(timeArr[1].split(':')[1])
    parseInt(timeArr[1].split(':')[2]);
    // 没0补齐
    const formatMonth: string = month < 10 ? `0${month}` : `${month}`
    const formatDay: string = day < 10 ? `0${day}` : `${day}`
    const formatHour: string = hour < 10 ? `0${hour}` : `${hour}`
    const formatMinute: string = minute < 10 ? `0${minute}` : `${minute}`
    if (nowYear === year && nowMonth === month && nowDay === day) {
        // 当天
        return `${formatHour}:${formatMinute}`
    }
    if (nowYear === year && nowMonth === month && nowDay - day === 1) {
        // 昨天
        return `昨天 ${formatHour}:${formatMinute}`
    }
    return `${formatMonth}-${formatDay} ${formatHour}:${formatMinute}`
}
