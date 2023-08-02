package com.jhc.chathub.utils;

import cn.hutool.core.lang.UUID;

public class FileUtils {
    // 生成随机文件名
    public static String generateRandomFileName() {
        return UUID.randomUUID().toString(true);
    }
}
