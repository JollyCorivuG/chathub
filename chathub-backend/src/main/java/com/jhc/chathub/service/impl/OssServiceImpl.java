package com.jhc.chathub.service.impl;

import cn.hutool.core.date.DateUtil;
import com.jhc.chathub.common.resp.Response;
import com.jhc.chathub.config.MinioProperties;
import com.jhc.chathub.service.IOssService;
import com.jhc.chathub.utils.FileUtils;
import com.jhc.chathub.utils.UserHolder;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class OssServiceImpl implements IOssService {
    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioProperties minioProperties;

    @Override
    public String getResignedObjectUrl(String bucketName, String objectName) throws Exception {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .method(Method.GET).build();
        return minioClient.getPresignedObjectUrl(args);
    }

    @Override
    public Response<String> uploadFile(MultipartFile file) {
        // 1.得到上传文件用户id和当天日期, 用于构建上传目录(用户id/日期)
        Long userId = UserHolder.getUser().getId();
        String date = DateUtil.format(new Date(), "yyyy_MM_dd");

        // 2.得到上传目录以及随机文件名
        String uploadDir = userId + "/" + date;
        String fileName = FileUtils.generateRandomFileName() + "-" + file.getOriginalFilename();

        // 3.上传文件
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(uploadDir + "/" + fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return Response.success(getResignedObjectUrl(minioProperties.getBucketName(), uploadDir + "/" + fileName));
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败");
        }
    }
}
