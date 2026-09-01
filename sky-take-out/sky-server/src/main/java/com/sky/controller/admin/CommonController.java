package com.sky.controller.admin;

import com.sky.config.MinioConfig;
import com.sky.result.Result;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@Slf4j
@Api(tags = "公共接口")
@RequestMapping("/api/common")
@AllArgsConstructor
public class CommonController {
    private final MinioConfig minioConfig;
    private final MinioClient minioClient;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> uploadFile(@RequestParam("file")MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try {
            String original = file.getOriginalFilename();
            String extension = null;
            if (original != null) {
                extension = original.substring(original.lastIndexOf("."));
            }
            String objectName = UUID.randomUUID().toString()+extension;
            minioClient.putObject(
                    PutObjectArgs.builder().
                            bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            String url = minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/" + objectName;
            return Result.success(url);
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage());
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
}
