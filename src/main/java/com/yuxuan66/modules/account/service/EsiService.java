package com.yuxuan66.modules.account.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.cache.CacheKey;
import com.yuxuan66.common.esi.EsiApi;
import com.yuxuan66.common.esi.entity.EsiTokenInfo;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.config.SystemConfig;
import com.yuxuan66.modules.account.entity.Account;
import com.yuxuan66.modules.account.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sir丶雨轩
 * @since 2022/1/5
 */
@Service
@RequiredArgsConstructor
public class EsiService {

    // 创建锁对象，保证多次点击只能同时处理一次
    private final ReentrantLock lock = new ReentrantLock();
    private final SystemConfig config;
    private final EsiApi esi;
    private final AccountService accountService;

    @Resource
    private AccountMapper accountMapper;

    /**
     * 构建esi授权地址
     *
     * @return es授权地址
     */
    public String buildAuthPath(String token) {
        String path = "https://login.eveonline.com/v2/oauth/authorize?response_type=code&redirect_uri=";
        path += config.getVal(CacheKey.Config.EVE.EVE_ESI_CALLBACK_PATH);
        path += "&client_id=";
        path += config.getVal(CacheKey.Config.EVE.EVE_ESI_CLIENT_ID);
        path += "&state=";
        path += TokenUtil.getLoginIdByToken(token);
        path += "&scope=";
        path += config.getVal(CacheKey.Config.EVE.EVE_ESI_SCOPE);
        return path;
    }


    /**
     * ESI授权回调
     *
     * @param code  ESI返回Code
     * @param state 授权传递State
     * @return 授权完成的地址
     */
    public String callback(String code, String state) throws InterruptedException {

        EsiTokenInfo tokenInfo = esi.codeToInfo(code);

        Long userId = Convert.toLong(state);
        if (userId == 0) {
            return getResultPath("授权地址错误，没有指定用户");
        }

        // 判断用户是否已经存在
        List<Account> accountList = accountMapper.selectList(new QueryWrapper<Account>().eq("character_id", tokenInfo.getCharacterId()));
        if (!accountList.isEmpty()) {
            if(Objects.equals(accountList.get(0).getCharacterId(), tokenInfo.getCharacterId())){
                Account updateToken = accountList.get(0);
                updateToken.setAccessToken(tokenInfo.getAccessToken());
                updateToken.setRefreshToken(tokenInfo.getRefreshToken());
                accountMapper.updateById(updateToken);
                accountService.refreshBaseInfo(accountList.get(0));
                return getResultPath("绑定成功,部分数据后台刷新中");
            }
            return getResultPath("绑定失败,角色已经绑定至其他账号");
        }
        Account account = new Account();
        account.setUserId(userId);
        account.setCharacterId(tokenInfo.getCharacterId());
        account.setCharacterName(tokenInfo.getName());
        account.setRefreshToken(tokenInfo.getRefreshToken());
        account.setAccessToken(tokenInfo.getAccessToken());
        account.setServer(1);
        account.setIsMain(false);
        accountMapper.insert(account);
        accountService.refreshBaseInfo(account);

        return getResultPath("绑定成功,部分数据后台刷新中");
    }

    /**
     * 获取成功的地址
     *
     * @param msg 提示信息
     * @return 地址
     */
    private String getResultPath(String msg) {
        return config.getVal(CacheKey.Config.WEB_PATH) + "showResult?msg=" + URLUtil.encode(msg);
    }


}
