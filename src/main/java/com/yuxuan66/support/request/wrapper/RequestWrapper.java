package com.yuxuan66.support.request.wrapper;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * {@link HttpServletRequest} 包装类，避免无法多次读取IO
 * @author Sir丶雨轩
 * @since 2021/6/23
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    /**
     * 参数字节数组
     */
    private byte[] requestBody;

    /**
     * Http请求对象
     */
    private final HttpServletRequest request;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.request = request;
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (null == this.requestBody) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(request.getInputStream(), byteArrayOutputStream);
            this.requestBody = byteArrayOutputStream.toByteArray();
        }

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

}
