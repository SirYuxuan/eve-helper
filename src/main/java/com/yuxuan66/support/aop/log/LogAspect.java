package com.yuxuan66.support.aop.log;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.net.NetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.common.utils.Lang;
import com.yuxuan66.common.utils.TokenUtil;
import com.yuxuan66.common.utils.WebUtil;
import com.yuxuan66.modules.system.entity.LogMapper;
import com.yuxuan66.modules.system.service.LogService;
import com.yuxuan66.modules.user.entity.User;
import com.yuxuan66.modules.user.mapper.UserMapper;
import com.yuxuan66.support.aop.log.annotation.Log;
import com.yuxuan66.support.basic.model.RespEntity;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Sir丶雨轩
 * @since 2021/12/7
 */
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {


    @Resource
    private UserMapper userMapper;
    @Resource
    private LogMapper logMapper;

    @Pointcut("@annotation( com.yuxuan66.support.aop.log.annotation.Log)")
    public void logCut() {
    }




    @Around(value = "logCut()")
    public Object saveSysLog(ProceedingJoinPoint joinPoint) {
        TimeInterval timeInterval =  DateUtil.timer();
        com.yuxuan66.modules.system.entity.Log saveLog = new com.yuxuan66.modules.system.entity.Log();

        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            return RespEntity.error("服务器错误，"+e.getMessage());
        }

        //获取操作
        Log log = method.getAnnotation(Log.class);
        if (log.type() == LogType.LOGIN) {
            String username = JSONArray.parseArray(params).getJSONObject(0).getString("username");
            List<User> userList = userMapper.selectList(new QueryWrapper<User>().eq("username", username));
            if (userList.isEmpty()) {
               return result;
            }
            saveLog.setCreateId(userList.get(0).getId());
            saveLog.setCreateBy(userList.get(0).getNickname());
        } else {
            saveLog.setCreateId(TokenUtil.getUserId());
            saveLog.setCreateBy(TokenUtil.getUser().getNickname());
        }
        UserAgent ua = UserAgent.parseUserAgentString(WebUtil.getRequest().getHeader("User-Agent"));
        saveLog.setIp(WebUtil.getIp());
        saveLog.setBrowser(ua.getBrowser().getName());
        saveLog.setCity(WebUtil.getCityInfo(saveLog.getIp()));
        saveLog.setParam(params);
        saveLog.setBody(JSON.toJSONString(result));
        saveLog.setCreateTime(Lang.getNow());
        saveLog.setTitle(log.value());
        saveLog.setTime(timeInterval.interval());

        logMapper.insert(saveLog);
        return result;
    }
}
