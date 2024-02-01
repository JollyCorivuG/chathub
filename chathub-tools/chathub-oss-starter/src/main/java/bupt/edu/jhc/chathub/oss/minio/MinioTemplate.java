package bupt.edu.jhc.chathub.oss.minio;

import bupt.edu.jhc.chathub.oss.FileUtils;
import bupt.edu.jhc.chathub.oss.OssProperties;
import cn.hutool.core.date.DateUtil;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @Description: minio  模板
 * @Author: <a href="https://github.com/JollyCorivuG">JollyCorivuG</a>
 * @CreateTime: 2024/1/22
 */
@AllArgsConstructor
public class MinioTemplate {

    private MinioClient minioClient;

    private OssProperties ossProperties;

    public String getResignedObjectUrl(String bucketName, String objectName) throws Exception {
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .method(Method.GET).build();
        return minioClient.getPresignedObjectUrl(args);
    }

    public String uploadFile(Long userId, MultipartFile file) {
        // 1.得到上传文件用户id和当天日期, 用于构建上传目录(用户id/日期)
        String date = DateUtil.format(new Date(), "yyyy_MM_dd");

        // 2.得到上传目录以及随机文件名
        String uploadDir = userId + "/" + date;
        String fileName = FileUtils.generateRandomFileName() + "-" + file.getOriginalFilename();

        // 3.上传文件
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(ossProperties.getBucketName())
                    .object(uploadDir + "/" + fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return getResignedObjectUrl(ossProperties.getBucketName(), uploadDir + "/" + fileName);
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败");
        }
    }
}
