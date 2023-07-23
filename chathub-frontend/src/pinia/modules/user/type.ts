import {UserInfo} from "@/api/user/type.ts";

export interface UserState {
    token: string,
    userInfo: UserInfo
}