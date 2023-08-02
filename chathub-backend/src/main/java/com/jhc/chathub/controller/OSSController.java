package com.jhc.chathub.controller;

import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.service.IOssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss")
@Tag(name = "OSS相关接口")
public class OSSController {
    @Resource
    private IOssService ossService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Response<String> uploadFile(@RequestParam("file")MultipartFile file) {
        return ossService.uploadFile(file);
    }
}
