package com.yuxuan66.support.mail;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.config.SystemConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Sir丶雨轩
 * @since 2021/12/8
 */
@Component
@RequiredArgsConstructor
public class MailHelper {

    private final SystemConfig config;

    /**
     * 获取邮件发送账户
     *
     * @return 账户
     */
    private MailAccount getAccount() {
        MailAccount mailAccount = new MailAccount();
        mailAccount.setAuth(true);
        mailAccount.setSslEnable(true);
        mailAccount.setUser(config.getVal(CacheKey.Config.Mail.MAIL_USER));
        mailAccount.setFrom(config.getVal(CacheKey.Config.Mail.MAIL_FROM));
        mailAccount.setPass(config.getVal(CacheKey.Config.Mail.MAIL_PASS));
        mailAccount.setHost(config.getVal(CacheKey.Config.Mail.MAIL_HOST));
        mailAccount.setPort(Convert.toInt(config.getVal(CacheKey.Config.Mail.MAIL_PORT)));

        return mailAccount;
    }

    /**
     * 给执行邮箱发送修改邮箱的验证码
     *
     * @return 验证码
     */
    public String sendUpdateUserMail(String receiver) {
        String code = RandomUtil.randomString(6).toUpperCase();

        try {
            Mail.create(getAccount())
                    .setTos(receiver)
                    .setTitle("邮箱验证")
                    .setContent(config.getVal(CacheKey.Config.Mail.MAIL_UPDATE_MAIL_TEMPLATE).
                            replace("{code}", code).replace("{name}", TokenUtil.getUser().getUsername()))
                    .setHtml(true)
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("发信服务器配置错误，请联系网站管理员");
        }
        return code;
    }

}
