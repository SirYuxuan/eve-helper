/*
 * Copyright 2013-2021 Sir丶雨轩
 *
 * This file is part of Sir丶雨轩/eve-corp-api.

 * Sir丶雨轩/eve-corp-api is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.

 * Sir丶雨轩/eve-corp-api is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Sir丶雨轩/eve-corp-api.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.yuxuan66.cache;

/**
 * 系统所有缓存的Key
 *
 * @author Sir丶雨轩
 * @since 2021/7/30
 */
public class CacheKey {

    /**
     * 系统配置
     */
    public static final class Config{

        /**
         * 系统核心配置
         */
        public static final class Admin{
            /**
             * 用户默认头像
             */
            public static final String USER_DEFAULT_AVATAR = "userDefaultAvatar";

            /**
             * rsa 私钥
             */
            public static final String RSA_PRIVATE = "rsaPrivate";
            /**
             * rsa公钥
             */
            public static final String RSA_PUBLIC = "rsaPublic";
            /**
             * 图片验证码过期时间
             */
            public static final String IMG_CODE_INVALID_TIME = "imgCodeInvalidTime";


        }

        /**
         * 手机号相关配置
         */
        public static final class Phone{
            /**
             * 手机号验证码过期时间
             */
            public static String PHONE_CODE_INVALID_TIME = "phoneCodeInvalidTime";
            /**
             * 短信发送的地域
             */
            public static String SMS_ENDPOINT = "smsEndpoint";
            /**
             * 短信签名
             */
            public static String SMS_SIGN = "smsSign";
            /**
             * 短信模板code
             */
            public static String SMS_TEMPLATE_CODE = "smsTemplateCode";
        }

        /**
         * 阿里云相关配置的Key
         */
        public static final class AliYun{


            /**
             * 阿里云OSS的地域
             */
            public static final String OSS_ENDPOINT = "ossEndpoint";

            /**
             * 阿里云OSS的bucket
             */
            public static final String OSS_BUCKET = "ossBucket";
            /**
             * 阿里云密钥id
             */
            public static final String ACCESS_KEY_ID = "accessKeyId";
            /**
             * 阿里云密码值
             */
            public static final String ACCESS_KEY_SECRET = "accessKeySecret";


        }


        /**
         * 邮件相关配置
         */
        public static final class Mail{


            /**
             * 邮件发送用户
             */
            public static final String MAIL_USER = "mailUser";

            /**
             * 邮件发送密码
             */
            public static final String MAIL_PASS = "mailPass";
            /**
             * 邮件发送人
             */
            public static final String MAIL_FROM = "mailFrom";
            /**
             * 邮件发送服务器地址
             */
            public static final String MAIL_HOST = "mailHost";
            /**
             * 邮件服务器端口
             */
            public static final String MAIL_PORT = "mailPort";
            /**
             * 修改用户邮箱的邮箱模板
             */
            public static final String MAIL_UPDATE_MAIL_TEMPLATE = "mailUpdateMailTemplate";
            /**
             * 邮件失效时间，单位秒
             */
            public static final String MAIL_CODE_INVALID_TIME = "mailCodeInvalidTime";


        }


    }


    /**
     * 邮箱验证码对照表
     */
    public static final String MAIL_CODE = "MAIL_CODE";

    /**
     * 验证码图片
     */
    public static final String CAPTCHA_CODE = "CAPTCHA_CODE";

    /**
     * 短信验证码
     */
    public static final String PHONE_CODE = "PHONE_CODE";

    /**
     * 缓存-系统-字典组
     */
    public static final String CACHE_SYS_DICT_GROUP = "SYS_DICT_GROUP_";

    /**
     * 缓存-系统-配置的map
     */
    public static final String CACHE_SYS_CONFIG_MAP = "CACHE_SYS_CONFIG_MAP";

}
