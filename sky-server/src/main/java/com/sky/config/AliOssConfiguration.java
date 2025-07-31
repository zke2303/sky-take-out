package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 对象存储服务配置类
 *
 * @Author itcast
 * @Create 2024/6/11
 **/
@Configuration
@Slf4j
public class AliOssConfiguration {
    //@Autowired
    //private AliOssProperties aliOssProperties;

    @Bean
    @ConditionalOnMissingBean //当spring容器中没有指定对象时，才执行此方法
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        log.info("开始创建OSS工具类...");
        return new AliOssUtil(
                aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
