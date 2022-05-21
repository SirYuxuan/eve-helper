package com.yuxuan66.support.aliyun;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.config.SystemConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Sir丶雨轩
 * @since 2021/12/9
 */
@Component
@RequiredArgsConstructor
public class SMSHelper {

    private final SystemConfig config;

    /**
     * TODO 创建阿里云客户端，后续可拆分给所有的阿里云服务
     * @return 客户端
     * @throws Exception 未知错误
     */
    public  Client createClient() throws Exception {
        Config aliConfig = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(config.getVal(CacheKey.Config.AliYun.ACCESS_KEY_ID))
                // 您的AccessKey Secret
                .setAccessKeySecret(config.getVal(CacheKey.Config.AliYun.ACCESS_KEY_SECRET));
        // 访问的域名
        aliConfig.endpoint = config.getVal(CacheKey.Config.Phone.SMS_ENDPOINT);
        return new Client(aliConfig);
    }

    /**
     * 发送短信验证码返回验证码，如果发送失败返回空
     * @param phone 手机号
     * @return
     * @throws Exception
     */
    public boolean sendSms(String phone) throws Exception {
        Client client = createClient();
        String code = Convert.toStr(RandomUtil.randomInt(1000,9999));
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(config.getVal(CacheKey.Config.Phone.SMS_SIGN))
                .setTemplateCode(config.getVal(CacheKey.Config.Phone.SMS_TEMPLATE_CODE))
                .setTemplateParam(code);
        SendSmsResponse response = client.sendSms(sendSmsRequest);
        System.out.println(response);
        return true;
    }
}
