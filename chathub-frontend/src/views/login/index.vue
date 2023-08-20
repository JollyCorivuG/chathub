<template>
    <div class="login-container">
        <div class="login-panel">
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
            <div v-if="loginType == 0" class="form">
                <van-form>
                    <van-cell-group inset>
                        <van-field
                            type="number"
                            label="账号"
                            placeholder="请输入账号"
                            v-model="loginForm.account"
                            :rules="rules.account"
                            right-icon="user-o"
                        />
                        <van-field
                            label="密码"
                            type="password"
                            placeholder="请输入密码"
                            v-model="loginForm.password"
                            :rules="rules.password"
                            right-icon="lock"
                        />
                    </van-cell-group>
                </van-form>
            </div>
            <div v-else class="phoneLoginForm">
                <van-form>
                    <van-cell-group inset>
                        <van-field
                            type="tel"
                            placeholder="请输入手机号"
                            right-icon="phone-o"
                            v-model="phoneLoginForm.phone"
                            :rules="phoneRules.phone"
                        />
                        <van-field
                            type="number"
                            center
                            placeholder="请输入短信验证码"
                            v-model="phoneLoginForm.code"
                            :rules="phoneRules.code"
                            right-icon="envelop-o"
                        >
                            <template #button>
                                <van-button size="small" :disabled="isGetCode" @click="getCode">{{getCodeButtonContent}}</van-button>
                            </template>
                        </van-field>
                    </van-cell-group>
                </van-form>
            </div>
            <div class="operator">
                <div class="need-register" @click="changeLoginType">
                    <span>{{ loginType == 0 ? '手机验证码登录': '账号密码登录' }}</span>
                    <svg x="1689954993368" class="icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="14" height="14"><path d="M761.6 489.6l-432-435.2c-9.6-9.6-25.6-9.6-35.2 0-9.6 9.6-9.6 25.6 0 35.2l416 416-416 425.6c-9.6 9.6-9.6 25.6 0 35.2s25.6 9.6 35.2 0l432-441.6C771.2 515.2 771.2 499.2 761.6 489.6z" fill="#7f7f7f"></path></svg>
                </div>
                <div class="login-button">
                    <van-button type="primary" @click="doLogin">登录</van-button>
                    <van-button @click="goRegisterPage" style="background-color: #e2e1e4;border: #e2e1e4">注册</van-button>
                </div>
            </div>
        </div>
    </div>
    <a href="https://beian.miit.gov.cn" target="_blank" class="beian">京ICP备2023021003号</a>
</template>

<script setup lang="ts">
import {ref} from 'vue'
import {CommonResponse, LoginByPhoneParams, LoginParams} from '@/api/user/type'
import {showNotify} from 'vant'
import useUserStore from '@/pinia/modules/user'
import {reqPhoneCode} from '@/api/user'
import {useRouter, useRoute} from "vue-router";

// 账号密码登录表单
const route = useRoute()
const loginForm = ref<LoginParams>({
    account: route.query.account as string | '',
    password: ''
})
const checkAccount = (val: string) => {
    const reg = /^\d{10}$/
    return reg.test(val)
}
const checkPassword = (val: string) => {
    const reg = /^.{6,16}$/
    return reg.test(val)
}
const rules = ref({
    account: [
        {validator: checkAccount, message: '账号为10位数字'}
    ],
    password: [
        {validator: checkPassword, message: '密码长度应为6-16位'}
    ]
})

// 切换登录方式
let loginType = ref<number>(0) // 0: 账号密码登录 1: 手机号验证码登录
const changeLoginType = () => {
    loginType.value = loginType.value == 0 ? 1 : 0
}

// 手机验证码登录表单
const phoneLoginForm = ref<LoginByPhoneParams>({
    phone: '',
    code: ''
})
const checkPhone = (val: string) => {
    // 手机号正则表达式
    const reg = /^1[3-9]\d{9}$/
    return reg.test(val)
}
const checkCode = (val: string) => {
    return val.length == 6
}
const phoneRules = ref({
    phone: [
        {validator: checkPhone, message: '请输入正确的手机号格式'}
    ],
    code: [
        {validator: checkCode, message: '验证码长度应为6位'}
    ]
})
let getCodeButtonContent = ref<string>('获取验证码')
let isGetCode = ref<boolean>(false)
const getCode = () => {
    if (!checkPhone(phoneLoginForm.value.phone)) {
        showNotify({type: 'danger', message: '请输入正确的手机号格式'})
        return
    }
    isGetCode.value = true
    getCodeButtonContent.value =  '60秒' + '后重试'
    // getCodeButtonContent的值会在倒计时结束后重置为'获取验证码'
    let count = 60
    const timer = setInterval(() => {
        count--
        getCodeButtonContent.value =  `${count}秒` + '后重试'
        if (count == 0) {
            clearInterval(timer)
            getCodeButtonContent.value = '获取验证码'
            isGetCode.value = false
        }
    }, 1000)
    reqPhoneCode(phoneLoginForm.value.phone)
}

// 登录业务 (账号密码登录)
const router = useRouter()
const userStore = useUserStore()
const doLogin = async () => {
    if (loginType.value == 0) {
        if (!checkAccount(loginForm.value.account) || !checkPassword(loginForm.value.password)) {
            showNotify({type: 'danger', message: '请检查表单正确性!'})
            return
        }
        const resp: CommonResponse = await userStore.login(loginForm.value)
        if (resp.statusCode == 0) {
            await router.push('/home/message')
        } else {
            showNotify({type: 'danger', message: resp.statusMsg})
        }
    } else {
        if (!checkPhone(phoneLoginForm.value.phone) || !checkCode(phoneLoginForm.value.code)) {
            showNotify({type: 'danger', message: '请检查表单正确性!'})
            return
        }
        const resp: CommonResponse = await userStore.loginByPhone(phoneLoginForm.value)
        if (resp.statusCode == 0) {
            await router.push('/home/message')
        } else {
            showNotify({type: 'danger', message: resp.statusMsg})
        }
    }
}

// 点击注册按钮, 进入注册页面
const goRegisterPage = () => {
    router.push('/register')
}

</script>

<style scoped lang="scss">
.login-container {
    background-size: cover;
    background: #F7F8FA url(https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png) no-repeat;
    background-size: cover;
    // 背景透明度
    height: 100%;
    display: flex;
    align-items: center;
    .login-panel {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 40px;
        .header {
            display: flex;
            align-items: center;
            justify-content: center;
            .logo {
                margin-right: 10px;
            }
            .title {
                color: black;
                font-weight: bold;
                font-size: 20px;
            }
        }
        .form {
            margin-top: 10px;
        }
        .phoneLoginForm {
            margin-top: 20px;
        }
        .operator {
            margin-top: 10px;
            display: flex;
            flex-direction: column;
            align-items: flex-end;
            width: 100%;
            .need-register {
                margin-right: 16px;
                display: flex;
                align-items: center;
                span {
                    color: #7f7f7f;
                    font-size: 14px;
                    margin-right: 2px;
                }
            }
            .login-button {
                display: flex;
                justify-content: space-evenly;
                margin-left: 7px;
                margin-right: 23px;
                width: calc(100% - 46px);
                margin-top: 15px;
                ::v-deep(.van-button) {
                    width: 46%;
                    height: 32px;
                }
            }

        }
    }
}
.beian {
    position: absolute;
    bottom: 10px;
    left: 50%;
    transform: translateX(-50%);
    color: #7f7f7f;

}
</style>