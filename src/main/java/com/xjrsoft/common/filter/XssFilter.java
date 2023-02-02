package com.xjrsoft.common.filter;

import com.xjrsoft.common.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.List;

;

@Slf4j
@Component
public class XssFilter implements Filter, Ordered {

    @Value("${xjrsoft.global-config.xssConfig.enabled}")
    private Boolean enabled;

    @Value("${xjrsoft.global-config.xssConfig.whiteIpList}")
    private List<String> whiteIpList;

    @Value("${xjrsoft.global-config.xssConfig.whiteApiList}")
    private List<String> whiteApiList;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //判断当前ip 是否存在于白名单
        if(!enabled){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String ip = IpUtil.getIpAddr(request);
        String url = request.getRequestURI();


        //判断当前ip 是否存在于白名单
        if(whiteIpList.contains(ip)){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        //判断当前url  是否存在于白名单
        if(whiteApiList.contains(url)){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //拦截该url并进行xss过滤
        XssHttpServletRequestWrapper xssHttpServletRequestWrapper = new XssHttpServletRequestWrapper(request);
        filterChain.doFilter(xssHttpServletRequestWrapper,servletResponse);

    }

    @Override
    public void destroy() {

    }

    @Override
    public int getOrder() {
        return 0;
    }
}

/**
 * xss过滤包装类
 */
@Slf4j
class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String strHeader = super.getHeader(name);
        if(StringUtils.isEmpty(strHeader)){
            return strHeader;

        }
        return Jsoup.clean(super.getHeader(name),Whitelist.relaxed());
    }

    @Override
    public String getParameter(String name) {
        String strParameter = super.getParameter(name);
        if(StringUtils.isEmpty(strParameter)){
            return strParameter;
        }
        return Jsoup.clean(super.getParameter(name),Whitelist.relaxed());
    }


    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if(values==null){
            return values;
        }
        int length = values.length;
        String[] escapseValues = new String[length];
        for(int i = 0;i<length;i++){
            //过滤一切可能的xss攻击字符串
            escapseValues[i] = Jsoup.clean(values[i], Whitelist.relaxed()).trim();
            if(!StringUtils.equals(escapseValues[i],values[i])){
                log.debug("xss字符串过滤前："+values[i]+"\r\n"+"过滤后："+escapseValues[i]);
            }
        }
        return escapseValues;
    }
}
