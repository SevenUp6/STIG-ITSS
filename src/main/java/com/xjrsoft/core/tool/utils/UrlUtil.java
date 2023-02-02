package com.xjrsoft.core.tool.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import org.springframework.web.util.UriUtils;

public class UrlUtil extends UriUtils {
    public UrlUtil() {
    }

    public static String encodeURL(String source, Charset charset) {
        return encode(source, charset.name());
    }

    public static String decodeURL(String source, Charset charset) {
        return decode(source, charset.name());
    }

    public static String getPath(String uriStr) {
        URI uri;
        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException var3) {
            throw new RuntimeException(var3);
        }

        return uri.getPath();
    }
}
