/**
 * Copyright 2016-2019 长沙湘北智造信息技术有限公司
 *
 * @version xbzz V2.0.28
 * @author 光华科技-软件研发部
 * @date：2019年10月12日
 * @url /db_link
 * @description：
 */
package com.xjrsoft.module.login.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
// 如果JDK版本是1.8,可使用原生Base64类
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.xjrsoft.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Copyright 2016-2019 长沙湘北智造信息技术有限公司
 *
 * @version xbzz V2.0.28
 * @author 光华科技-软件研发部
 * @date：2019年10月12日
 * @description：
 *    核心方法 ：sendSmsSimple
 */
@Component
public class SmsHuaWei {
    // 无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    // 无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";
    @Value("${sms.huawei.url}")
    private String url;
    @Value("${sms.huawei.appKey}")
    private String appKey;
    @Value("${sms.huawei.appSecret}")
    private String appSecret;
    //验证码类发送人手机号通道号
    @Value("${sms.huawei.sender}")
    private String sender;
    //通知类发送人手机号通道号
    @Value("${sms.huawei.messagesender}")
    private String messagesender;
    @Value("${sms.huawei.login.templateId}")
    private String loginTemplateId;
    @Value("${sms.huawei.message.templateId}")
    private String messageTemplateId;
    @Value("${sms.huawei.signature}")
    private String signature;

    /**
     *
     * Copyright 2016-2019 长沙湘北智造信息技术有限公司
     *
     * @version xbzz V2.0.28
     * @author 光华科技-软件研发部
     * @date：2019年10月12日
     * @description：
     *  phone : 18111111111   必须是11位数字
     *  code : 123456  必须是6位数字  验证码
     * @return
     *  正常情况：{"result":[{"originTo":"+8618173120968","createTime":"2019-11-06T03:10:02Z","from":"8819110542643","smsMsgId":"294350a2-6484-4544-abd0-369d7de36cb3_11546660","status":"000000"}],"code":"000000","description":"Success"}
     *  发生错误：返回 ""
     */
    public String sendSmsSimple(String phone, String code) {
        if (StringUtil.isNumeric(code) && StringUtil.isNumeric(phone) && phone.length() == 11 && code.length() == 6) {
            final String url = this.url.trim(); // APP接入地址+接口访问URI
            final String appKey = this.appKey.trim(); // APP_Key
            final String appSecret = this.appSecret.trim(); // APP_Secret
            final String sender = this.sender.trim(); // 国内短信签名通道号或国际/港澳台短信通道号
            final String templateId = loginTemplateId.trim(); // 模板ID

            // 条件必填,国内短信关注,当templateId指定的模板类型为通用模板时生效且必填,必须是已审核通过的,与模板类型一致的签名名称
            // 国际/港澳台短信不用关注该参数
            final String signature = this.signature.trim(); // 签名名称

            return sendSms(url, appKey, appSecret, sender, templateId, signature, "+86" + phone, "", "[\"" + code + "\"]");
        } else {
            return "";
        }
    }

    @SuppressWarnings("finally")
    private String  sendSms(String url, String appKey, String appSecret, String sender, String templateId, String signature, String receiver, String statusCallBack, String templateParas) {
        // 请求Body,不携带签名名称时,signature请填null
        String body = buildRequestBody(sender, receiver, templateId, templateParas, statusCallBack, signature);
        if (null == body || body.isEmpty()) {
            return "";
        }
        // 请求Headers中的X-WSSE参数值
        String wsseHeader = buildWsseHeader(appKey, appSecret);
        if (null == wsseHeader || wsseHeader.isEmpty()) {
            return "";
        }
        Writer out = null;
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        HttpsURLConnection connection = null;
        InputStream is = null;

        // 为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        try {
            trustAllHttpsCertificates();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            URL realUrl = new URL(url);
            connection = (HttpsURLConnection) realUrl.openConnection();
            connection.setHostnameVerifier(hv);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            // 请求方法
            connection.setRequestMethod("POST");
            // 请求Headers参数
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", AUTH_HEADER_VALUE);
            connection.setRequestProperty("X-WSSE", wsseHeader);
            connection.connect();
            out = new OutputStreamWriter(connection.getOutputStream());
            out.write(body); // 发送请求Body参数
            out.flush();
            out.close();

            int status = connection.getResponseCode();
            if (200 == status) { // 200
                is = connection.getInputStream();
            } else { // 400/401
                is = connection.getErrorStream();
            }
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != is) {
                    is.close();
                }
                if (null != in) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result.toString();
        }

    }

    /**
     * 构造请求Body体
     *
     * @param sender
     * @param receiver
     * @param templateId
     * @param templateParas
     * @param statusCallBack
     * @param signature
     *            | 签名名称,使用国内短信通用模板时填写
     * @return
     */
    static String buildRequestBody(String sender, String receiver, String templateId, String templateParas, String statusCallBack, String signature) {
        if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty() || templateId.isEmpty()) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("from", sender);
        map.put("to", receiver);
        map.put("templateId", templateId);
        if (null != templateParas && !templateParas.isEmpty()) {
            map.put("templateParas", templateParas);
        }
        if (null != statusCallBack && !statusCallBack.isEmpty()) {
            map.put("statusCallback", statusCallBack);
        }
        if (null != signature && !signature.isEmpty()) {
            map.put("signature", signature);
        }
        StringBuilder sb = new StringBuilder();
        String temp = "";
        for (String s : map.keySet()) {
            try {
                temp = URLEncoder.encode(map.get(s), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append(s).append("=").append(temp).append("&");
        }

        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * 构造X-WSSE参数值
     *
     * @param appKey
     * @param appSecret
     * @return
     */
    static String buildWsseHeader(String appKey, String appSecret) {
        if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
            System.out.println("buildWsseHeader(): appKey or appSecret is null.");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date()); // Created
        String nonce = UUID.randomUUID().toString().replace("-", ""); // Nonce

        MessageDigest md;
        byte[] passwordDigest = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update((nonce + time + appSecret).getBytes());
            passwordDigest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(passwordDigest); // PasswordDigest
        // 如果JDK版本低于1.8,请加载三方库提供Base64类,并使用如下代码
        // String passwordDigestBase64Str = Base64.encodeBase64String(passwordDigest);
        // //PasswordDigest
        // 若passwordDigestBase64Str中包含换行符,请执行如下代码进行修正
        // passwordDigestBase64Str = passwordDigestBase64Str.replaceAll("[\\s*\t\n\r]",
        // "");
        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }

    /**
     * 忽略证书信任问题
     *
     * @throws Exception
     */
    void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return;
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    public String sendMessageByHuaWei(String phone, String message) {
        final String url = this.url.trim(); // APP接入地址+接口访问URI
        final String appKey = this.appKey.trim(); // APP_Key
        final String appSecret = this.appSecret.trim(); // APP_Secret
        final String sender = this.messagesender.trim(); // 国内短信签名通道号或国际/港澳台短信通道号
        final String templateId = messageTemplateId.trim(); // 模板ID
        // 条件必填,国内短信关注,当templateId指定的模板类型为通用模板时生效且必填,必须是已审核通过的,与模板类型一致的签名名称
        // 国际/港澳台短信不用关注该参数
        return sendSms(url, appKey, appSecret, sender, templateId, null, "+86" + phone, "", "[\"" + message + "\"]");
    }
}
