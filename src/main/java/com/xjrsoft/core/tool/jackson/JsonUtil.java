package com.xjrsoft.core.tool.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xjrsoft.core.tool.utils.Exceptions;
import com.xjrsoft.core.tool.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;

public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    public JsonUtil() {
    }

    public static <T> String toJson(T value) {
        try {
            return getInstance().writeValueAsString(value);
        } catch (Exception var2) {
            log.error(var2.getMessage(), var2);
            return null;
        }
    }

    public static byte[] toJsonAsBytes(Object object) {
        try {
            return getInstance().writeValueAsBytes(object);
        } catch (JsonProcessingException var2) {
            throw Exceptions.unchecked(var2);
        }
    }

    public static <T> T parse(String content, Class<T> valueType) {
        try {
            return getInstance().readValue(content, valueType);
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
            return null;
        }
    }

    public static <T> T parse(String content, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(content, typeReference);
        } catch (IOException var3) {
            throw Exceptions.unchecked(var3);
        }
    }

    public static <T> T parse(byte[] bytes, Class<T> valueType) {
        try {
            return getInstance().readValue(bytes, valueType);
        } catch (IOException var3) {
            throw Exceptions.unchecked(var3);
        }
    }

    public static <T> T parse(byte[] bytes, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(bytes, typeReference);
        } catch (IOException var3) {
            throw Exceptions.unchecked(var3);
        }
    }

    public static <T> T parse(InputStream in, Class<T> valueType) {
        try {
            return getInstance().readValue(in, valueType);
        } catch (IOException var3) {
            throw Exceptions.unchecked(var3);
        }
    }

    public static <T> T parse(InputStream in, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(in, typeReference);
        } catch (IOException var3) {
            throw Exceptions.unchecked(var3);
        }
    }

    public static <T> List<T> parseArray(String content, Class<T> valueTypeRef) {
        try {
            if (!StringUtil.startsWithIgnoreCase(content, "[")) {
                content = "[" + content + "]";
            }

            List<Map<String, Object>> list = (List)getInstance().readValue(content, new TypeReference<List<Map<String, Object>>>() {
            });
            List<T> result = new ArrayList();
            Iterator var4 = list.iterator();

            while(var4.hasNext()) {
                Map<String, Object> map = (Map)var4.next();
                result.add(toPojo(map, valueTypeRef));
            }

            return result;
        } catch (IOException var6) {
            log.error(var6.getMessage(), var6);
            return null;
        }
    }

    public static Map<String, Object> toMap(String content) {
        try {
            return (Map)getInstance().readValue(content, Map.class);
        } catch (IOException var2) {
            log.error(var2.getMessage(), var2);
            return null;
        }
    }

    public static <T> Map<String, T> toMap(String content, Class<T> valueTypeRef) {
        try {
            Map<String, Map<String, Object>> map = (Map)getInstance().readValue(content, new TypeReference<Map<String, Map<String, Object>>>() {
            });
            Map<String, T> result = new HashMap(16);
            Iterator var4 = map.entrySet().iterator();

            while(var4.hasNext()) {
                Entry<String, Map<String, Object>> entry = (Entry)var4.next();
                result.put(entry.getKey(), toPojo((Map)entry.getValue(), valueTypeRef));
            }

            return result;
        } catch (IOException var6) {
            log.error(var6.getMessage(), var6);
            return null;
        }
    }

    public static <T> T toPojo(Map fromValue, Class<T> toValueType) {
        return getInstance().convertValue(fromValue, toValueType);
    }

    public static JsonNode readTree(String jsonString) {
        try {
            return getInstance().readTree(jsonString);
        } catch (IOException var2) {
            throw Exceptions.unchecked(var2);
        }
    }

    public static JsonNode readTree(InputStream in) {
        try {
            return getInstance().readTree(in);
        } catch (IOException var2) {
            throw Exceptions.unchecked(var2);
        }
    }

    public static JsonNode readTree(byte[] content) {
        try {
            return getInstance().readTree(content);
        } catch (IOException var2) {
            throw Exceptions.unchecked(var2);
        }
    }

    public static JsonNode readTree(JsonParser jsonParser) {
        try {
            return (JsonNode)getInstance().readTree(jsonParser);
        } catch (IOException var2) {
            throw Exceptions.unchecked(var2);
        }
    }

    public static ObjectMapper getInstance() {
        return JsonUtil.JacksonHolder.INSTANCE;
    }

    public static class JacksonObjectMapper extends ObjectMapper {
        private static final long serialVersionUID = 4288193147502386170L;
        private static final Locale CHINA;

        public JacksonObjectMapper() {
            super.setLocale(CHINA);
            super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            super.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA));
            super.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
            super.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
            super.findAndRegisterModules();
            super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            super.configure(Feature.ALLOW_SINGLE_QUOTES, true);
            super.getDeserializationConfig().withoutFeatures(new DeserializationFeature[]{DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES});
            super.registerModule(new XjrJavaTimeModule());
            super.findAndRegisterModules();
        }

        public ObjectMapper copy() {
            return super.copy();
        }

        static {
            CHINA = Locale.CHINA;
        }
    }

    private static class JacksonHolder {
        private static ObjectMapper INSTANCE = new JsonUtil.JacksonObjectMapper();

        private JacksonHolder() {
        }
    }
}
