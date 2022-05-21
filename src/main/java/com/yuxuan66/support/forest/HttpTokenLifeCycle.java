package com.yuxuan66.support.forest;

import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.lifecycles.MethodAnnotationLifeCycle;
import com.dtflys.forest.reflection.ForestMethod;
import com.yuxuan66.common.esi.EsiApi;
import com.yuxuan66.common.utils.SpringUtil;
import com.yuxuan66.modules.account.entity.Account;

/**
 * @author Sir丶雨轩
 * @since 2022/1/6
 */
public class HttpTokenLifeCycle implements MethodAnnotationLifeCycle<Token, Object> {


    @Override
    public boolean beforeExecute(ForestRequest request) {
        Object user = request.argument(0);
        if (user instanceof Account) {
            Account account = (Account) user;
            EsiApi esi = SpringUtil.getBean(EsiApi.class);
            if(!esi.refreshToken(account)){
                return false;
            }
            request.addHeader("Authorization", "Bearer " + account.getAccessToken());
        }
        return true;
    }

    @Override
    public void onMethodInitialized(ForestMethod method, Token annotation) {

    }
}
