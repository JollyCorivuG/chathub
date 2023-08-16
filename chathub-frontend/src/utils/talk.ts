import {UserInfo} from "@/api/user/type.ts";

export const getLikePeople = (latestLikeUsers: UserInfo[], totalNum: number): string => {
    // 将点赞的人拼接成字符串用、分隔
    let likePeople: string = ''
    latestLikeUsers.forEach((item: UserInfo, index: number) => {
        if (index === latestLikeUsers.length - 1) {
            likePeople += item.nickName
        } else {
            likePeople += item.nickName + '、'
        }
    })

    // 如果人数超过5人就截取前5人, 并且采用...代替, 后面加上等xx人点赞
    if (totalNum > 5) {
        return likePeople.slice(0, 5) + '...' + '等' + totalNum + '人点赞'
    } else {
        if (totalNum === 0) {
            return ''
        }
        return likePeople + '等' + totalNum + '人点赞'
    }
}