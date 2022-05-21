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
            /**
             * 邮件模板-购买LP通知
             */
            public static final String MAIL_BUY_LP_GOODS_TEMPLATE = "mailBuyLpGoodsTemplate";
            /**
             * 购买LP通知的接收人
             */
            public static final String MAIL_BUY_LP_GOODS_MANAGER = "mailBuyLpGoodsManager";
            /**
             * 邮件模板-老僵尸回归通知
             */
            public static final String MAIL_CORP_CORPSE = "mailCorpCorpCorpse";


        }

        /**
         * lp发放相关配置
         */
        public static final class LP{
            /**
             * 在线获得积分
             */
            public static final String ONLINE = "lpNumOnline";
            /**
             * 离线获得积分
             */
            public static final String OFFLINE="lpNumOffline";
            /**
             * 刷怪量每亿ISK转换的DKP
             */
            public static final String DKP_RAT="dkpRat";
            /**
             * 贸易量每亿ISK转换的DKP
             */
            public static final String DKP_SHOP="dkpShop";
            /**
             * 挖矿量每亿ISK转换的DKP
             */
            public static final String DKP_MINING="dkpMining";
            /**
             * 挖矿量每亿ISK转换的DKP
             */
            public static final String DKP_PAP="dkpPap";
            public static final String DKP_LOST="dkpLost";
            public static final String DKP_DANGER="dkpDanger";
            public static final String DKP_DESTROY="dkpDestroy";
        }

        /**
         * eve相关配置
         */
        public static final class EVE{

            /**
             * SDE保存的目录
             */
            public static final String EVE_SDE_PATH = "eveSdePath";

            /**
             * EVE ESI ClientID
             */
            public static String EVE_ESI_CLIENT_ID = "eveEsiClientId";
            /**
             * EVE ESI ecretKey
             */
            public static String EVE_ESI_SECRET_KEY = "eveEsiSecretKey";
            /**
             * EVE ESI 授权回调
             */
            public static String EVE_ESI_CALLBACK_PATH = "eveEsiCallbackPath";
            /**
             * EVE ESI 授权范围
             */
            public static String EVE_ESI_SCOPE = "eveEsiScope";
            /**
             * EVE补损最大KM时间，单位天
             */
            public static String EVE_SRP_MAX_KM_DAY = "eveSrpMaxKmDay";
            /**
             * EVE主军团id
             */
            public static String EVE_MAIN_CORP = "eveMainCorp";

        }

        /**
         * 前端项目地址
         */
        public static String WEB_PATH = "webPath";

        /**
         * 机器人账号
         */
        public static String BOT_QQ = "botQQ";
        /**
         * 机器人密码
         */
        public static String BOT_PASS = "botPass";
        /**
         * 跟联盟网站通讯的Cookie
         */
        public static String PAP_COOKIE = "papCookie";


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

    /**
     * 换成-esi授权用户id
     */
    public static final String CACHE_ESI_AUTH_ID = "ESI_AUTH:";

    /**
     * eve角色id对应的名字
     */
    public static final String CACHE_EVE_CHARACTERS_NAME_ID = "EVE_CHARACTERS:";
    public static final String CACHE_EVE_STATION_NAME_ID = "EVE_STATION:";
    public static final String CACHE_EVE_REGION_NAME_ID = "EVE_REGION:";
    public static final String CACHE_EVE_STRUCTURES_NAME_ID = "EVE_STRUCTURES:";

    /**
     * 军团成员所对应的头衔
     */
    public static final String CACHE_EVE_CORP_BASIC_TITLES = "EVE_CORP:BASIC:TITLES:";

    /**
     * eve星系id对应的名字
     */
    public static final String CACHE_EVE_SYSTEM_NAME_ID = "EVE_SYSTEM_NAME:";

    /**
     * eve sde type id
     */
    public static final String EVE_SDE_TYPE_ID = "EVE:SDE:TYPE_ID";
    /**
     * eve sde 地址对照
     */
    public static final String EVE_SDE_ADDRESS_NAME = "EVE:SDE:ADDRESS_NAME";
    /**
     * eve sde 蓝图数据
     */
    public static final String EVE_SDE_BLUE_PRINT = "EVE:SDE:BLUE_PRINT";
    public static final String EVE_CONTRACT_PUSH = "EVE:CONTRACT_PUSH:";

    /**
     * eve sde 分类 id
     */
    public static final String EVE_SDE_CATEGORY_ID = "EVE:SDE:CATEGORY_ID";
    /**
     * eve sde 分组缓存key
     */
    public static final String EVE_SDE_GROUP_ID = "EVE:SDE:GROUP_ID";
    /**
     * eve sde 元分组缓存key
     */
    public static final String EVE_SDE_META_GROUP_ID = "EVE:SDE:META_GROUP_ID";

    /**
     * eve sde 市场分组缓存key
     */
    public static final String EVE_SDE_MARKET_GROUP_ID = "EVE:SDE:MARKET_GROUP_ID";

}
