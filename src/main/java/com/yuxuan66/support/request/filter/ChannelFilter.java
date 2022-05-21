package com.yuxuan66.support.request.filter;

import com.yuxuan66.support.request.wrapper.RequestWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 对每个请求包装{@link HttpServletRequest}对象
 * @author Sir丶雨轩
 * @since 2021/6/23
 */
@WebFilter(filterName = "channelFilter", urlPatterns = "/*")
public class ChannelFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            ServletRequest requestWrapper = null;
            if (servletRequest instanceof HttpServletRequest) {
                requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
            }
            if (requestWrapper == null) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                filterChain.doFilter(requestWrapper, servletResponse);
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }
}
