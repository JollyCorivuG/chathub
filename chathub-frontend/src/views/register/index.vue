<template>
    <div class="register-container">
        <div class="register-panel">
            <div class="header">
                <div class="logo">
                    <van-image
                        width="45"
                        height="45"
                        fit="cover"
                        src="/logo.png"
                        round
                    />
                </div>
                <div class="title">ChatHub</div>
            </div>
            <div class="register-form">
                <van-form>
                    <van-cell-group inset>
                        <van-field
                            type="tel"
                            label="手机号"
                            placeholder="请输入手机号"
                            right-icon="phone-o"
                            v-model="registerForm.phone"
                            :rules="rules.phone"
                        />
                        <van-field
                            type="number"
                            label="账号"
                            placeholder="请输入账号"
                            right-icon="user-o"
                            v-model="registerForm.account"
                            :rules="rules.account"
                        />
                        <van-field
                            label="密码"
                            type="password"
                            placeholder="请输入密码"
                            right-icon="lock"
                            v-model="registerForm.password"
                            :rules="rules.password"
                        />
                    </van-cell-group>
                </van-form>
            </div>
            <div class="operator">
                <div class="to-login">
                    <svg @click="goLoginPage" x="1690077973181" class="icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="14" height="14"><path d="M700.371228 394.525472 174.028569 394.525472l255.952416-227.506551c12.389168-11.011798 13.505595-29.980825 2.492774-42.369993-11.011798-12.386098-29.977755-13.506619-42.367947-2.492774L76.425623 400.975371c-6.960529 5.496178-11.434423 14.003945-11.434423 23.561625 0 0.013303 0.001023 0.026606 0.001023 0.039909 0 0.01228-0.001023 0.025583-0.001023 0.037862 0 0.473791 0.01535 0.946558 0.037862 1.418302 0.001023 0.016373 0.001023 0.032746 0.001023 0.049119 0.39295 8.030907 3.992941 15.595186 10.034541 20.962427l315.040163 280.028764c5.717212 5.083785 12.83533 7.580652 19.925818 7.580652 8.274454 0 16.514115-3.403516 22.442128-10.07445 11.011798-12.387122 9.896394-31.357172-2.492774-42.367947l-256.128425-227.665163 526.518668 0c109.219517 0 198.075241 88.855724 198.075241 198.075241s-88.855724 198.075241-198.075241 198.075241L354.324888 850.696955c-16.57449 0-30.011524 13.437034-30.011524 30.011524s13.437034 30.011524 30.011524 30.011524l346.046341 0c142.31631 0 258.098289-115.783003 258.098289-258.098289S842.686515 394.525472 700.371228 394.525472z" fill="#7f7f7f"></path></svg>
                    <span @click="goLoginPage">返回登录</span>
                </div>
                <div class="register-button">
                    <van-button type="primary" @click="doRegister">注册</van-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import {ref} from "vue";
import {RegisterParams, CommonResponse} from "@/api/user/type.ts";
import {showNotify} from "vant";
import useUserStore from "@/pinia/modules/user";

// 注册表单
const registerForm = ref<RegisterParams>({
    phone: '',
    account: '',
    password: ''
})
const checkPhone = (val: string) => {
    const reg = /^1[3-9]\d{9}$/
    return reg.test(val)
}
const checkAccount = (val: string) => {
    const reg = /^\d{10}$/
    return reg.test(val)
}
const checkPassword = (val: string) => {
    const reg = /^.{6,16}$/
    return reg.test(val)
}
const rules = ref({
    phone: [
        {validator: checkPhone, message: '请输入正确的手机号格式'}
    ],
    account: [
        {validator: checkAccount, message: '账号为10位数字'}
    ],
    password: [
        {validator: checkPassword, message: '密码长度应为6-16位'}
    ]
})

// 注册业务
const userStore = useUserStore()
const router = useRouter()
const doRegister = async () => {
    if (!checkPhone(registerForm.value.phone) || !checkAccount(registerForm.value.account) || !checkPassword(registerForm.value.password)) {
        showNotify({type: 'danger',message: '请检查表单正确性!'})
        return
    }
    const resp: CommonResponse = await userStore.register(registerForm.value)
    if (resp.statusCode == 0) {
        await router.push('/home/message')
    } else {
        showNotify({type: 'danger',message: resp.statusMsg})
    }
}

// 返回登录页面
const goLoginPage = () => {
    router.push('/login')
}
</script>

<style scoped lang="scss">
.register-container {
    background-color: #F7F8FA;
    height: 100%;
    display: flex;
    align-items: center;
    .register-panel {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 40px;
        .header {
            display: flex;
            align-items: center;
            .logo {
                margin-right: 10px;
            }
            .title {
                color: black;
                font-weight: bold;
                font-size: 20px;
            }
        }
        .register-form {
            margin-top: 10px;
        }
        .operator {
            width: 100%;
            padding: 0 18px;
            margin-top: 10px;
            display: flex;
            flex-direction: column;
            .to-login {
                display: flex;
                align-items: center;
                span {
                    font-size: 14px;
                    color: #7f7f7f;
                    margin-left: 2px;
                }
            }
            .register-button {
                width: 100%;
                margin-top: 15px;
                ::v-deep(.van-button) {
                    width: 100%;
                    height: 32px;
                }
            }
        }
    }

}
</style>