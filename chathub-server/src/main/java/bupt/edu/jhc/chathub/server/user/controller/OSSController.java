package bupt.edu.jhc.chathub.server.user.controller;

import bupt.edu.jhc.chathub.common.domain.vo.resp.Response;
import bupt.edu.jhc.chathub.common.utils.context.RequestHolder;
import bupt.edu.jhc.chathub.oss.minio.MinioTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Description: OSS 接口
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@RestController
@RequestMapping("/api/oss")
@Api(tags = "OSS相关接口")
public class OSSController {
    @Resource
    private MinioTemplate minioTemplate;


    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Response<String> uploadFile(@RequestParam("file") MultipartFile file) {
        Long userId = RequestHolder.get().getUid();
        return Response.success(minioTemplate.uploadFile(userId, file));
    }
}