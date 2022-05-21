package com.yuxuan66.support.aliyun;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.config.SystemConfig;
import lombok.RequiredArgsConstructor;
import okhttp3.Cache;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Date;

/**
 * 阿里云OSS帮助类
 *
 * @author Sir丶雨轩
 * @since 2021/12/8
 */
@Component
@RequiredArgsConstructor
public class OSSHelper {

    private final SystemConfig config;

    /**
     * 上传一个字节流到阿里云，文件自动重命名
     *
     * @param bytes    字节流
     * @param fileName 文件名
     * @return 新文件名
     */
    public String upload(byte[] bytes, String fileName) {
        OSS ossClient = new OSSClientBuilder().build(config.getVal(CacheKey.Config.AliYun.OSS_ENDPOINT), config.getVal(CacheKey.Config.AliYun.ACCESS_KEY_ID), config.getVal(CacheKey.Config.AliYun.ACCESS_KEY_SECRET));

        String ext = FileUtil.getSuffix(fileName);
        String newFileName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN) + "." + ext;

        ossClient.putObject(config.getVal(CacheKey.Config.AliYun.OSS_BUCKET), newFileName, new ByteArrayInputStream(bytes));

        ossClient.shutdown();

        String url = config.getVal(CacheKey.Config.AliYun.OSS_ENDPOINT).replace("https://", "");
        url = "https://" + config.getVal(CacheKey.Config.AliYun.OSS_BUCKET) + "." + url;

        return url + "/" + newFileName;
    }

}
