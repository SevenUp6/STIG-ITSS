package com.xjrsoft.core.tool.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.io.FileUtil;
import com.xjrsoft.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xjrsoft.core.tool.jackson.JsonUtil;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.WebUtils;

public class WebUtil extends WebUtils {
    private static final Logger log = LoggerFactory.getLogger(WebUtil.class);
    public static final String USER_AGENT_HEADER = "user-agent";
    public static final String UN_KNOWN = "unknown";

    public WebUtil() {
    }

    public static boolean isBody(HandlerMethod handlerMethod) {
        ResponseBody responseBody = (ResponseBody)ClassUtil.getAnnotation(handlerMethod, ResponseBody.class);
        return responseBody != null;
    }

    @Nullable
    public static String getCookieVal(String name) {
        HttpServletRequest request = getRequest();
        Assert.notNull(request, "request from RequestContextHolder is null");
        return getCookieVal(request, name);
    }

    @Nullable
    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, (String)null, 0);
    }

    public static void setCookie(HttpServletResponse response, String name, @Nullable String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : ((ServletRequestAttributes)requestAttributes).getRequest();
    }

    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : ((ServletRequestAttributes)requestAttributes).getResponse();
    }

    public static void renderJson(HttpServletResponse response, Object result) {
        renderJson(response, result, "application/json;charset=UTF-8");
    }

    public static void renderJson(HttpServletResponse response, Object result, String contentType) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);

        try {
            PrintWriter out = response.getWriter();
            Throwable var4 = null;

            try {
                out.append(JsonUtil.toJson(result));
            } catch (Throwable var14) {
                var4 = var14;
                throw var14;
            } finally {
                if (out != null) {
                    if (var4 != null) {
                        try {
                            out.close();
                        } catch (Throwable var13) {
                            var4.addSuppressed(var13);
                        }
                    } else {
                        out.close();
                    }
                }

            }
        } catch (IOException var16) {
            log.error(var16.getMessage(), var16);
        }

    }

    public static String getIP() {
        return getIP(getRequest());
    }

    @Nullable
    public static String getIP(HttpServletRequest request) {
        Assert.notNull(request, "HttpServletRequest is null");
        String ip = request.getHeader("X-Requested-For");
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }

        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return StringUtil.isBlank(ip) ? null : ip.split(",")[0];
    }

    public static String getRequestParamString(HttpServletRequest request) {
        try {
            return getRequestStr(request);
        } catch (Exception var2) {
            return "";
        }
    }

    public static String getRequestStr(HttpServletRequest request) throws IOException {
        String queryString = request.getQueryString();
        return StringUtil.isNotBlank(queryString) ? (new String(queryString.getBytes(Charsets.ISO_8859_1), Charsets.UTF_8)).replaceAll("&amp;", "&").replaceAll("%22", "\"") : getRequestStr(request, getRequestBytes(request));
    }

    public static byte[] getRequestBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        } else {
            byte[] buffer = new byte[contentLength];

            int readlen;
            for(int i = 0; i < contentLength; i += readlen) {
                readlen = request.getInputStream().read(buffer, i, contentLength - i);
                if (readlen == -1) {
                    break;
                }
            }

            return buffer;
        }
    }

    public static String getRequestStr(HttpServletRequest request, byte[] buffer) throws IOException {
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }

        String str = (new String(buffer, charEncoding)).trim();
        if (StringUtil.isBlank(str)) {
            StringBuilder sb = new StringBuilder();
            Enumeration parameterNames = request.getParameterNames();

            while(parameterNames.hasMoreElements()) {
                String key = (String)parameterNames.nextElement();
                String value = request.getParameter(key);
                StringUtil.appendBuilder(sb, new CharSequence[]{key, "=", value, "&"});
            }

            str = StringUtil.removeSuffix(sb.toString(), "&");
        }

        return str.replaceAll("&amp;", "&");
    }

    public static void writeFileToResponse(String fileName, File file) throws IOException {
        HttpServletResponse response = getResponse();
        if (!StringUtil.isEmpty(fileName)) {
            String suffix = StringUtils.substringAfterLast(fileName, StringPool.DOT);
//            if("apk".equals(suffix)){
//                response.setHeader("Content-Disposition",
//                        "attachment;fileName=" + java.net.URLEncoder.encode("" + fileName , "UTF-8"));
//            }else{

                response.setHeader("Content-Disposition", "inline;fileName=\"" + new String((fileName).getBytes("utf-8"), "ISO8859-1") + "\"");
//            }
        }
        FileUtil.writeToStream(file, response.getOutputStream());
    }
}
