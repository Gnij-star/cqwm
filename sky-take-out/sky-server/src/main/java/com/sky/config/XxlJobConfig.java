package com.sky.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "xxl.job")
@Data
@Slf4j
public class XxlJobConfig {

    private String accessToken;        // xxl.job.access-token 或 xxl.job.accessToken
    private Admin admin = new Admin();
    private Executor executor = new Executor();

    @Data
    public static class Admin {
        private String addresses;      // xxl.job.admin.addresses
    }

    @Data
    public static class Executor {
        private String appname;        // xxl.job.executor.appname
        private Integer port;
        private String logpath;
        private Integer logretentiondays;// xxl.job.executor.port
    }

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        executor.setAdminAddresses(this.admin.getAddresses());
        executor.setAppname(this.executor.getAppname());
        executor.setPort(this.executor.getPort());
        executor.setAccessToken(this.accessToken);
        executor.setLogPath(this.executor.getLogpath());
        executor.setLogRetentionDays(this.executor.getLogretentiondays());
        // 👇 加这行日志，确认配置真的注入了
        log.info("XXL-JOB config: adminAddresses={}, appname={}, port={}, accessToken={}",
                this.admin.getAddresses(), this.executor.getAppname(),
                this.executor.getPort(), this.accessToken);
        return executor;
    }
}