<template>
    <div class="login-container">
        <div class="login-panel">
            <div class="header">
                <div class="logo">

                </div>
                <div class="title">Chat Hub</div>
            </div>
            <div class="form">
                <van-form>
                    <van-cell-group inset>
                        <van-field
                            label="账号"
                        />
                        <van-field
                            label="密码"
                        />
                    </van-cell-group>
                </van-form>
            </div>
            <div class="operator">
                <div class="need-register">
                    <span>还没账号？立即注册</span>
                    <svg x="1689954993368" class="icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="14" height="14"><path d="M761.6 489.6l-432-435.2c-9.6-9.6-25.6-9.6-35.2 0-9.6 9.6-9.6 25.6 0 35.2l416 416-416 425.6c-9.6 9.6-9.6 25.6 0 35.2s25.6 9.6 35.2 0l432-441.6C771.2 515.2 771.2 499.2 761.6 489.6z" fill="#7f7f7f"></path></svg>
                </div>
                <div class="login-button">
                    <van-button round type="primary">登录</van-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">

</script>

<style scoped lang="scss">
.login-container {
    background-color: #F7F8FA;
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
            .title {
                color: black;
                font-weight: bold;
                font-size: 20px;
            }
        }
        .form {
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
                margin-left: 7px;
                margin-right: 23px;
                width: calc(100% - 46px);
                margin-top: 10px;
                ::v-deep(.van-button) {
                    width: 100%;
                    height: 32px;
                }
            }

        }
    }
}
</style>