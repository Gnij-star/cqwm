package com.sky.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO 配置类
 * 从 application.yml 中读取 minio 开头的配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    /**
     * MinIO 服务地址（如：http://localhost:9000）
     */
    private String endpoint;

    /**
     * 访问密钥（Access Key）
     */
    private String accessKey;

    /**
     * 密钥（Secret Key）
     */
    private String secretKey;

    /**
     * 默认存储桶名称
     */
    private String bucketName;

    /**
     * 创建 MinIO 客户端 Bean
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}