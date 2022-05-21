package com.yuxuan66.support.exception;

import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.crypto.CryptoException;
import com.yuxuan66.common.utils.WebUtil;
import com.yuxuan66.support.basic.model.RespEntity;
import com.yuxuan66.support.constant.Support;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.crypto.BadPaddingException;
import java.util.Objects;

/**
 * 全局异常处理，返回友好的提示
 * TODO 需要添加日志系统，保存错误日志
 * @author Sir丶雨轩
 * @since 2021/6/23
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<RespEntity> handleResourceNotFoundException(NoHandlerFoundException e) {
        return buildResponseEntity(RespEntity.error("接口" + e.getRequestURL() + "不存在"));
    }

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<RespEntity> handleException(Throwable e) {

        log.error(e.getMessage(), e);

        if(e.getClass().equals(HttpRequestMethodNotSupportedException.class)){
            HttpRequestMethodNotSupportedException methodNotSupportedException = (HttpRequestMethodNotSupportedException) e;
            return buildResponseEntity(RespEntity.error("无法找到接口"+ WebUtil.getRequest().getRequestURI()+"的"+methodNotSupportedException.getMethod()+"请求方式"));
        }

        if(e.getMessage().contains(Support.Exception.DB_EXEC_ERROR)){
            return buildResponseEntity(RespEntity.error("数据库执行失败"));
        }

        return buildResponseEntity(RespEntity.error(e.getMessage()));
    }

    /**
     * 账号相关异常
     *
     * @param e SaToken框架异常
     * @return 错误提示
     */
    @ExceptionHandler(SaTokenException.class)
    public ResponseEntity<RespEntity> handleSaTokenException(SaTokenException e) {
        return buildResponseEntity(RespEntity.notLogin(e.getMessage().replace("token", "账号")));
    }

    /**
     * rsa解密错误
     * @param e 解密错误
     * @return 错误提示
     */
    @ExceptionHandler(CryptoException.class)
    public ResponseEntity<RespEntity> handleBadPaddingException(CryptoException e) {
        return buildResponseEntity(RespEntity.error("密钥错误,无法正常解密"));
    }

    /**
     * 处理所有接口数据验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespEntity> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 打印堆栈信息
        log.error(e.getMessage(), e);
        String[] str = Objects.requireNonNull(e.getBindingResult().getAllErrors().get(0).getCodes())[1].split("\\.");
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        String msg = "不能为空";
        if (msg.equals(message)) {
            message = str[1] + ":" + message;
        }
        return buildResponseEntity(RespEntity.error(message));
    }

    /**
     * 统一返回
     */
    private ResponseEntity<RespEntity> buildResponseEntity(RespEntity respEntity) {
        return new ResponseEntity<>(respEntity, HttpStatus.OK);
    }
}
