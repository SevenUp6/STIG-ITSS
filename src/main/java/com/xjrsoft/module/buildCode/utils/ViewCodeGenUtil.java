package com.xjrsoft.module.buildCode.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import org.apache.commons.collections.MapUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 前端代码生成工具类
 */
public final class ViewCodeGenUtil {

    private ViewCodeGenUtil(){}

    public static final String LOGIN_FOLDER_PATH = File.separator+ "pages" + File.separator + "login";

    public static final String INDEX_FOLDER_PATH = File.separator+ "pages" + File.separator + "basics";

    public static String genAppLoginViewCode(Map<String, Object> config) {
        StringBuilder code = new StringBuilder();

        code.append("<template>\r\n");
        code.append("    <view class=\"login\">\r\n");
        code.append("       <view class=\"content\">\r\n");
        code.append("           <view class=\"login-form\">\r\n");
        code.append("               <block v-if=\"loginType === 1\">\r\n");
        code.append("                   <xjr-login v-model=\"txt_mobile\" :accountConfig=\"accountConfig\" style=\"margin-bottom: "+(MapUtils.getInteger(config, "marginBottom") * 2) +"upx; \"></xjr-login>\r\n");
        code.append("                   <xjr-password v-model=\"txt_password\" :passwordConfig=\"passwordConfig\" style=\"margin-bottom: " + (MapUtils.getInteger(config, "marginBottom") * 2) + "upx; \"></xjr-password>\r\n");
        code.append("               </block>\r\n");
        code.append("               <block v-else>\r\n");
        code.append("                   <xjr-login v-model=\"txt_mobile\" :accountConfig=\"mobileConfig\" style=\"margin-bottom: " + ((MapUtils.getInteger(config, "marginBottom") * 2) + "upx; \"></xjr-login>\r\n"));
        code.append("                   <xjr-msm v-model=\"txt_msm\" :msmConfig=\"msmConfig\" :tel=\"txt_mobile\" style=\"margin-bottom: " + ((MapUtils.getInteger(config, "marginBottom") * 2) + "upx;\" ></xjr-msm>\r\n"));
        code.append("               </block>\r\n");
        code.append("               <view v-if=\"setting.tenantEnabled\" class=\"login-yzm\"><i class=\"" + (MapUtils.getString(config, "tenantIcon") + " fl\"></i><input class=\"fl\" style=\"font-size: 36upx; boxShadow:" + (MapUtils.getInteger(config, "shadow") == 1 ? "0px 1px 10px 1px #ffffff;" : "") + (MapUtils.getInteger(config, "isBorder") == 1 ? "borderWidth:" + MapUtils.getInteger(config, "borderWidth") + "px;borderColor:" + MapUtils.getString(config, "borderColor") + ";borderRadius:" + MapUtils.getInteger(config, "radius") : "") + ";\" placeholder=\"" + MapUtils.getString(config, "tenantTips") + "\" v-model=\"txt_tenantCode\" name=\"input\"/></view>\r\n"));
        code.append("               <view @tap=\"login\" class=\"btn\"> <text style=\"margin-left:10px;\">" + MapUtils.getString(config, "buttonName") + "</text> </view>\r\n");
        code.append("               <view @tap=\"changeLoginType\" class=\"login_type\"> <text>{{loginType === 1 ? '手机号登录' : '帐号密码'}}</text></view>\r\n");
        code.append("           </view>			\r\n");
        code.append("           <view class=\"footer\">"+MapUtils.getString(config, "footer")+"</view>\r\n");
        code.append("       </view>\r\n");
        code.append("    </view>   \r\n");
        code.append("</template>\r\n\r\n");

        code.append("<script>\r\n");

        code.append("    import {setting} from '@/appsetting.js';\r\n");
        code.append("    import xjrLogin from \"../../components/login/account-input/xjrAccount.vue\"\r\n\r\n");
        code.append("    import xjrPassword from \"../../components/login/password-input/xjrPassword.vue\"\r\n\r\n");
        code.append("    import xjrMsm from \"../../components/login/account-msm/xjrMsm.vue\"\r\n\r\n");
        code.append("    import md5 from \"js-md5\"\r\n\r\n");

        code.append("export default {\r\n");
        code.append("    data() {\r\n");
        code.append("        return {\r\n");
        code.append("            setting,\r\n");
        code.append("            txt_mobile: \"\",\r\n");
        code.append("            txt_password: \"\",\r\n");
        code.append("            txt_msm: \"\",\r\n");
        code.append("            txt_tenantCode: \"\",\r\n");
        code.append("            loginType : 1,  //登录方式  1  帐号密码登录 或者  0 手机号码登录\r\n");
        code.append("            accountConfig: {\r\n");
        code.append("               placeholder: \""+ MapUtils.getString(config, "accountTips") + "\",\r\n");
        code.append("               icon: \""+MapUtils.getString(config, "accountIcon") +"\",\r\n");
        code.append("               verify: \"\",\r\n");
        code.append("               height: \"" + MapUtils.getString(config, "inputHeight") + "px\",\r\n");
        code.append("               shadow: " + MapUtils.getInteger(config, "shadow") + ",\r\n");
        code.append("               isBorder: " + MapUtils.getInteger(config, "isBorder") + ",\r\n");
        code.append("               borderWidth: "+ MapUtils.getInteger(config, "borderWidth") + ",\r\n");
        code.append("               borderColor: \"" + MapUtils.getString(config, "borderColor") + "\",\r\n");
        code.append("               radius: " + MapUtils.getInteger(config, "radius") + ",\r\n");
        code.append("            },\r\n");
        code.append("            mobileConfig: {\r\n");
        code.append("               placeholder: \"请输入手机号码\",\r\n");
        code.append("               icon: \"fa fa-mobile\",\r\n");
        code.append("               verify: \"\",\r\n");
        code.append("               height: \"" + MapUtils.getString(config, "inputHeight") + "px\",\r\n");
        code.append("               shadow: " + MapUtils.getInteger(config, "shadow") + ",\r\n");
        code.append("               isBorder: " + MapUtils.getInteger(config, "isBorder") + ",\r\n");
        code.append("               borderWidth: " + MapUtils.getInteger(config, "borderWidth") + ",\r\n");
        code.append("               borderColor: \"" + MapUtils.getString(config, "borderColor") + "\",\r\n");
        code.append("               radius: " + MapUtils.getInteger(config, "radius") + ",\r\n");
        code.append("            },\r\n");
        code.append("            passwordConfig: {\r\n");
        code.append("               placeholder: \""+ MapUtils.getString(config, "passwordTips") + "\",\r\n");
        code.append("               icon: \""+MapUtils.getString(config, "passwordIcon") +"\",\r\n");
        code.append("               verify: \"\",\r\n");
        code.append("               height: \"" + MapUtils.getString(config, "inputHeight") + "px\",\r\n");
        code.append("               shadow: " + MapUtils.getInteger(config, "shadow") + ",\r\n");
        code.append("               isBorder: " + MapUtils.getInteger(config, "isBorder") + ",\r\n");
        code.append("               borderWidth: " + MapUtils.getInteger(config, "borderWidth") + ",\r\n");
        code.append("               borderColor: \"" + MapUtils.getString(config, "borderColor") + "\",\r\n");
        code.append("               radius: " + MapUtils.getInteger(config, "radius") + ",\r\n");
        code.append("            },\r\n");
        code.append("            msmConfig: {\r\n");
        code.append("               placeholder: \"" + MapUtils.getString(config, "msmTips") + "\",\r\n");
        code.append("               icon: \"" + MapUtils.getString(config, "msmIcon") + "\",\r\n");
        code.append("               verify: \"\",\r\n");
        code.append("               height: \"" + MapUtils.getString(config, "inputHeight") + "px\",\r\n");
        code.append("               shadow: " + MapUtils.getInteger(config, "shadow") + ",\r\n");
        code.append("               isBorder: " + MapUtils.getInteger(config, "isBorder") + ",\r\n");
        code.append("               borderWidth: " + MapUtils.getInteger(config, "borderWidth") + ",\r\n");
        code.append("               borderColor: \"" + MapUtils.getString(config, "borderColor") + "\",\r\n");
        code.append("               radius: " + MapUtils.getInteger(config, "radius") + ",\r\n");
        code.append("            },\r\n");
        code.append("        }\r\n");
        code.append("    },\r\n");
        code.append("    methods: {\r\n");
        code.append("       changeLoginType(){\r\n");
        code.append("           this.loginType = this.loginType == 0 ? 1 : 0\r\n");
        code.append("       },		\r\n");
        code.append("       login() {\r\n");
        code.append("           const that = this\r\n");
        code.append("           if (!this.txt_mobile) {\r\n");
        code.append("               uni.showToast({\r\n");
        code.append("                   title: \"帐号填写不正确\",\r\n");
        code.append("                   icon: \"none\",\r\n");
        code.append("                   position: \"top\"\r\n");
        code.append("               })\r\n");
        code.append("               return;\r\n");
        code.append("           }\r\n");
        code.append("          if(this.loginType === 1){\r\n");
        code.append("               if (!this.txt_password) {\r\n");
        code.append("                 uni.showToast({\r\n");
        code.append("                     title: \"密码填写不正确\",\r\n");
        code.append("                     icon: \"none\",\r\n");
        code.append("                     position: \"top\"\r\n");
        code.append("                   })\r\n");
        code.append("                   return;\r\n");
        code.append("               }\r\n");
        code.append("           }\r\n");
        code.append("           else{\r\n");
        code.append("               if (!this.txt_msm) {\r\n");
        code.append("                 uni.showToast({\r\n");
        code.append("                     title: \"验证码填写不正确\",\r\n");
        code.append("                     icon: \"none\",\r\n");
        code.append("                     position: \"top\"\r\n");
        code.append("                   })\r\n");
        code.append("                   return;\r\n");
        code.append("               }\r\n");
        code.append("           }\r\n");
        code.append("           if (setting.tenantEnabled) {\r\n");
        code.append("               if (!this.txt_tenantCode) {\r\n");
        code.append("                   uni.showToast({\r\n");
        code.append("                       title: \"必须填写租户码\",\r\n");
        code.append("                       icon: \"none\",\r\n");
        code.append("                       position: \"top\"\r\n");
        code.append("                   })\r\n");
        code.append("                   return;\r\n");
        code.append("               }\r\n");
        code.append("           }\r\n");
        code.append("           if(this.loginType === 1){\r\n");
        code.append("               uni.request({\r\n");
        code.append("                   url: setting.baseURL + \"/login\", ///login/doLogin\r\n");
        code.append("                   method: 'POST',\r\n");
        code.append("                   data: {\r\n");
        code.append("                       account: that.txt_mobile,\r\n");
        code.append("                       password: md5(that.txt_password),\r\n");
        code.append("                       code: that.txt_tenantCode\r\n");
        code.append("                   },\r\n");
        code.append("                   success: (res) => {\r\n");
        code.append("                       if (res.data.success) {\r\n");
        code.append("                           uni.setStorageSync(\"token\", res.data.data.Token);\r\n");
        code.append("                           uni.setStorageSync(\"mobile\", that.txt_mobile)\r\n");
        code.append("                           uni.setStorageSync(\"user_info\", res.data.data.UserInfo);\r\n");
        code.append("                           uni.setStorageSync(\"roles\", res.data.data.F_Role);\r\n");
        code.append("                           uni.setStorageSync(\"company\", res.data.data.F_Company);\r\n");
        code.append("                           uni.setStorageSync(\"department\", res.data.data.F_Department);\r\n");
        code.append("                           that.setData()\r\n");
        code.append("                           uni.switchTab({ url: \"../basics/index\" })\r\n");
        code.append("                           uni.setStorageSync(\"company\", res.data.data.F_Company);\r\n");
        code.append("                      } else {\r\n");
        code.append("                          uni.showToast({\r\n");
        code.append("                              title: res.data.msg,\r\n");
        code.append("                              icon: \"none\",\r\n");
        code.append("                              position: \"top\"\r\n");
        code.append("                           })\r\n");
        code.append("                           return;\r\n");
        code.append("                      }\r\n");
        code.append("                  }\r\n");
        code.append("               });\r\n");
        code.append("           }\r\n");
        code.append("           else{\r\n");
        code.append("               uni.request({\r\n");
        code.append("                   url: setting.baseURL + \"/login/phone\", ///login/doLogin\r\n");
        code.append("                   method: 'POST',\r\n");
        code.append("                   data: {\r\n");
        code.append("                       account: that.txt_mobile,\r\n");
        code.append("                       password: that.txt_msm,\r\n");
        code.append("                       code: that.txt_tenantCode\r\n");
        code.append("                   },\r\n");
        code.append("                   success: (res) => {\r\n");
        code.append("                       if (res.data.success) {\r\n");
        code.append("                           uni.setStorageSync(\"token\", res.data.data.Token);\r\n");
        code.append("                           uni.setStorageSync(\"mobile\", that.txt_mobile)\r\n");
        code.append("                           uni.setStorageSync(\"user_info\", res.data.data.UserInfo);\r\n");
        code.append("                           uni.setStorageSync(\"roles\", res.data.data.F_Role);\r\n");
        code.append("                           uni.setStorageSync(\"company\", res.data.data.F_Company);\r\n");
        code.append("                           uni.setStorageSync(\"department\", res.data.data.F_Department);\r\n");
        code.append("                           that.setData()\r\n");
        code.append("                           uni.switchTab({ url: \"../basics/index\" })\r\n");
        code.append("                           uni.setStorageSync(\"company\", res.data.data.F_Company);\r\n");
        code.append("                      } else {\r\n");
        code.append("                          uni.showToast({\r\n");
        code.append("                              title: res.data.msg,\r\n");
        code.append("                              icon: \"none\",\r\n");
        code.append("                              position: \"top\"\r\n");
        code.append("                           })\r\n");
        code.append("                           return;\r\n");
        code.append("                      }\r\n");
        code.append("                  }\r\n");
        code.append("               });\r\n");
        code.append("              }\r\n");
        code.append("           },\r\n");
        code.append("           setData() {\r\n");
        code.append("               const value = uni.getStorageSync('module_list');\r\n");
        code.append("               if (!value) {\r\n");
        code.append("               uni.setStorage({\r\n");
        code.append("                   key: 'module_list',\r\n");
        code.append("                   data: [{ name: \"功能列表\",icon: \"settingsfill\",url: \"more-app\",color: 'green',isshow: true,type: 'other'},{ name: \"组件演示\",icon: \"roundrightfill\",url: \"module/module-show\",color: 'cyan',isshow: true,type: 'other'}]\r\n");
        code.append("               });\r\n");
        code.append("          }\r\n");

        code.append("    },\r\n");

        code.append("   },\r\n\r\n");
        code.append("    components: {\r\n");
        code.append("        xjrLogin,\r\n");
        code.append("        xjrPassword,\r\n");
        code.append("        xjrMsm\r\n");
        code.append("    }\r\n");
        code.append("}\r\n\r\n");
        code.append("</script>\r\n");

        code.append("<style>\r\n");
        code.append("   .login{\r\n");
        code.append("       background-image: url(" + MapUtils.getString(config, "backgroundImg") + ");\r\n");
        code.append("       background-repeat: no-repeat;\r\n");
        code.append("       background-size:contain;\r\n");
        code.append("       background-position: top;\r\n");
        code.append("       background-color: #fff;\r\n");
        code.append("       height: 100%;\r\n");
        code.append("       width: 100%;\r\n");
        code.append("   }\r\n");
        code.append("   page {\r\n");
        code.append("       height: 100%;");
        code.append("       background: #fff;\r\n");
        code.append("   }\r\n");

        code.append("   .content {\r\n");
        code.append("       bottom: "+(MapUtils.getInteger(config, "top") * 2)+ "upx;\r\n");
        code.append("       width: 630upx;\r\n");
        code.append("       margin: 0px auto;\r\n");
        code.append("       position: absolute;\r\n");
        code.append("       left: 0px;\r\n");
        code.append("       right: 0px;\r\n");
        code.append("   }\r\n");

        code.append("   .footer {\r\n");
        code.append("       position: fixed;\r\n");
        code.append("       bottom: 15px;\r\n");
        code.append("       left: 0;\r\n");
        code.append("       text-align: center;\r\n");
        code.append("       width: 100%;\r\n");
        code.append("       color: #ccc;\r\n");
        code.append("       font-size: 12px;\r\n");
        code.append("   }\r\n");

        code.append("   .logo {\r\n");
        code.append("       text-align: center;\r\n");
        code.append("       margin: 0rpx auto 30rpx auto;\r\n");
        code.append("   }\r\n");

        code.append("   .logo image {\r\n");
        code.append("       width: 100%;\r\n");
        code.append("   }\r\n");

        code.append("   .login-form .btn {\r\n");
        code.append("       margin: "+(MapUtils.getInteger(config, "btnMarginTop") * 2)+ "rpx auto " + (MapUtils.getInteger(config, "btnMarginBottom") * 2) + "rpx;\r\n");
        code.append("       text-align: center;\r\n");
        code.append("       height: " + (MapUtils.getInteger(config, "btnHeight") * 2) + "rpx;\r\n");
        code.append("       line-height: 110rpx;\r\n");
        code.append("       border-radius: " + (MapUtils.getInteger(config, "btnRadius") * 2) + "rpx;\r\n");
        code.append("       background-color: #02A7F0;\r\n");
        code.append("       background-image: linear-gradient(to right, " + MapUtils.getString(config, "btnBgColor1") + ", " + MapUtils.getString(config, "btnBgColor2") + ");\r\n");
        code.append("       box-shadow: 5px 6px 12px rgba(0, 107, 217, 0.2);\r\n");
        code.append("       width: 630upx;\r\n");
        code.append("       color: " + MapUtils.getString(config, "btnNameColor") + ";\r\n");
        code.append("       font-size: " + (MapUtils.getInteger(config, "btnFontSize") * 2) + "rpx;\r\n");
        code.append("       letter-spacing: 10px;\r\n");
        code.append("   }\r\n");

        code.append("   .login_type{\r\n");
        code.append("       text-align: center;\r\n");
        code.append("       color: #0081FF;\r\n");
        code.append("       padding-top:5upx;\r\n");
        code.append("   }\r\n");
        code.append("   .fogetPass {\r\n");
        code.append("       width: 630upx;\r\n");
        code.append("       margin: 30rpx auto;\r\n");
        code.append("   }\r\n");

        code.append("   .login-third .text {\r\n");
        code.append("       margin: 137rpx auto 106rpx auto;\r\n");
        code.append("       text-align: center;\r\n");
        code.append("   }\r\n");

        code.append("   .login-third .flex {\r\n");
        code.append("       text-align: center;\r\n");
        code.append("       display: flex;\r\n");
        code.append("       justify-content: space-around;\r\n");
        code.append("   }\r\n");

        code.append("   .login-third .flex image {\r\n");
        code.append("       width: 88rpx;\r\n");
        code.append("       height: 88rpx;\r\n");
        code.append("   }\r\n");

        code.append("   .login-yzm {\r\n");
        code.append("       margin: 0 auto;\r\n");
        code.append("       width: 630upx;\r\n");
        code.append("       height: " + MapUtils.getInteger(config, "inputHeight")*2 + "upx;\r\n");
        code.append("       line-height: " + MapUtils.getInteger(config, "inputHeight")*2 + "upx;\r\n");
        code.append("       border-radius: " + MapUtils.getInteger(config, "radius")*2 + "upx;\r\n");
        code.append("       border: 1px solid rgb(235, 235, 235);\r\n");
        code.append("       margin-bottom: " + MapUtils.getInteger(config, "marginBottom")*2 + "upx;\r\n");
        code.append("       background: #fff;\r\n");

        code.append("   }\r\n");

        code.append("   .login-yzm i {\r\n");
        code.append("       width: 100upx;\r\n");
        code.append("       height: " + MapUtils.getInteger(config, "inputHeight")*2 + "upx;\r\n");
        code.append("       text-align: center;\r\n");
        code.append("       line-height: " + MapUtils.getInteger(config, "inputHeight")*2 + "upx;\r\n");
        code.append("       font-size: 40upx;\r\n");
        code.append("       color: #ccc;\r\n");
        code.append("   }\r\n");

        code.append("   .uni-input-placeholder,\r\n");
        code.append("   .uni-textarea-placeholder {\r\n");
        code.append("       color: grey !important;\r\n");

        code.append("   }\r\n");

        code.append("   ::v-deep .login-form input {\r\n");
        code.append("       color: #666;\r\n");
        code.append("       height: " + (MapUtils.getInteger(config, "inputHeight")*2 - 4) + "upx;\r\n");
        code.append("       line-height: " + (MapUtils.getInteger(config, "inputHeight")*2 - 4) + "upx;\r\n");
        code.append("       flex: 1;\r\n");
        code.append("   }\r\n");
        code.append("   ::v-deep .login-form .item .icon {\r\n");
        code.append("       width: 100upx;\r\n");
        code.append("       height: " + (MapUtils.getInteger(config, "inputHeight")*2 - 4) + "upx;\r\n");
        code.append("       text-align: center;\r\n");
        code.append("       line-height: " + (MapUtils.getInteger(config, "inputHeight")*2 - 4) + "upx;\r\n");
        code.append("       font-size: 40upx;\r\n");
        code.append("       color: #ccc;\r\n");
        code.append("   }\r\n");

        code.append("   ::v-deep .input-placeholder {\r\n");
        code.append("       color: #999;\r\n");
        code.append("       line-height: " + (MapUtils.getInteger(config, "inputHeight")*2 - 4) + "upx;\r\n");
        code.append("       height: "  + (MapUtils.getInteger(config, "inputHeight")*2 - 4) + "upx;\r\n");
        code.append("       margin: auto;\r\n");
        code.append("   }\r\n");
        code.append("</style>\r\n");

        return code.toString();
    }

    public static String genAppIndexViewCode(List<Map<String, Object>> components) {
        String configStr = StringPool.EMPTY;
        String importStr = StringPool.EMPTY;
        String componentStr = StringPool.EMPTY;

        StringBuilder code = new StringBuilder();

        code.append("<template>\r\n");
        code.append("    <view class=\"page\">\r\n");

        for (Map<String, Object> component : components)
        {
            String componentType = MapUtils.getString(component, "type");
            //  轮播图 开始
            if (StringUtil.equalsIgnoreCase(componentType, "solidImg"))
            {

                code.append("    <xjr-solidimg :componentConfig = \"" + MapUtils.getString(component, "id") + "Config\"  class=\"margin-bottom-sm\"></xjr-solidimg>\r\n");

                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" height: %1$s, ", MapUtils.getString(component, "height"));
                configStr += String.format(" configType: %1$s, ", MapUtils.getInteger(component, "config", 0) == 1 ? "'manual'" : "'code'");
                configStr += String.format(" databaselinkId: '%1$s', ", MapUtils.getInteger(component, "config", 0) == 1 ? "" : MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" imgField: '%1$s', ", MapUtils.getString(component, "imgUrl"));
                configStr += String.format(" urlField: '%1$s', ", MapUtils.getString(component, "imgAddr"));

                //如果是手动配置  才会增加以下配置
                if (MapUtils.getInteger(component, "config", 0) == 1)
                {
                    List<String> listImg = (List<String>) component.get("imgs");

                    JSONArray dataList = new JSONArray();
                    for (String img : listImg) {
                        JSONObject data = new JSONObject();
                        data.put("img", img);
                        data.put("to", "");
                        data.put("name", "");
                        dataList.add(data);
                    }

                    configStr += String.format(" dataList: %1$s, ", dataList.toJSONString());
                }
                configStr += "}, \r\n";


                if (!importStr.contains("xjrSolidimg"))
                {
                    importStr += "import xjrSolidimg from \"@/components/solid/solidshowImg.vue\"; \r\n";

                    componentStr += "xjrSolidimg,";
                }

            }
            //  轮播图 结束

            //  轮播文字 开始
            if (StringUtil.equalsIgnoreCase(componentType, "solidText"))
            {
                code.append("    <xjr-solidtext :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\"  class=\"margin-bottom-sm\"></xjr-solidtext>\r\n");

                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" height: %1$s, \r\n", MapUtils.getInteger(component, "height"));
                configStr += String.format(" configType: %1$s, \r\n", MapUtils.getInteger(component, "config", 0) == 1 ? "'manual'" : "'code'");
                configStr += String.format(" textColor: %1$s, \r\n", JSONObject.toJSON(component.get("textcolor")).toString());

                //手动配置不需要这两个
                if (MapUtils.getInteger(component, "config", 0) == 2) {
                    configStr += String.format(" databaselinkId: '%1$s', \r\n", MapUtils.getInteger(component, "config", 0) == 1 ? "" : MapUtils.getString(component, "F_DatabaseLinkId"));
                    configStr += String.format(" sql: '%1$s', \r\n", MapUtils.getString(component, "sql").replace("\r\n", ""));
                    configStr += String.format(" textField: '%1$s', \r\n", MapUtils.getString(component, "info"));
                    configStr += String.format(" urlField: '%1$s', \r\n", MapUtils.getString(component, "imgAddr"));
                    configStr += String.format(" keyfield: '%1$s', \r\n", MapUtils.getString(component, "keyfield"));
                }

                //如果是手动配置  才会增加以下配置
                if (MapUtils.getInteger(component, "config", 0) == 1)
                {

                    List<String> titleList = (List<String>) component.get("titles");
                    List<String> linkList = (List<String>) component.get("links");
                    List<String> colorList = (List<String>) component.get("textcolor");

                    JSONArray dataList = new JSONArray();

                    for(int i = 0; i < titleList.size(); i ++)
                    {
                        JSONObject data = new JSONObject();
                        data.put("text", titleList.get(i));
                        data.put("to", linkList.get(i));
                        data.put("color", colorList.get(i));
                        dataList.add(data);
                    }

                    configStr += String.format(" dataList: %1$s, \r\n", dataList.toJSONString());
                }

                configStr += "}, \r\n";


                if (!importStr.contains("xjrSolidtext"))
                {
                    importStr += "import xjrSolidtext from \"@/components/solid/solidshowtext.vue\"; \r\n";


                    componentStr += "xjrSolidtext,";
                }
            }
            // 轮播文字结束

            //  滑动组件 开始
            if (StringUtil.equalsIgnoreCase(componentType, "swiperComponent"))
            {

                code.append("    <xjr-solidcomponent :componentConfig = \"" + MapUtils.getString(component, "id") + "Config\"  class=\"margin-bottom-sm\"></xjr-solidcomponent>\r\n");

                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" height: %1$s, ", MapUtils.getInteger(component, "height"));
                configStr += String.format(" title: '%1$s', ", MapUtils.getString(component, "title"));
                configStr += String.format(" databaselinkId: '%1$s', ", MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" imgField: '%1$s', ", MapUtils.getString(component, "imgUrl"));
                configStr += String.format(" titleFiled: '%1$s', ", MapUtils.getString(component, "info"));
                configStr += String.format(" contentField: '%1$s', ", MapUtils.getString(component, "content"));
                configStr += String.format(" urlField: '%1$s', ", MapUtils.getString(component, "link"));
                configStr += String.format(" keyfield: '%1$s', \r\n", MapUtils.getString(component, "keyfield"));
                configStr += String.format(" num: '%1$s', \r\n", MapUtils.getInteger(component, "num"));

                configStr += "}, ";


                if (!importStr.contains("xjrSolidcomponent"))
                {
                    importStr += "import xjrSolidcomponent from \"@/components/solid/solidcomponent.vue\"; \r\n";

                    componentStr += "xjrSolidcomponent,";
                }

            }
            //  滑动组件 结束

            // 图文卡片开始
            if (StringUtil.equalsIgnoreCase(componentType, "imgCard"))
            {
                code.append("    <xjr-imgcard :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\"  class=\"margin-bottom-sm\"></xjr-imgcard>\r\n");

                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" number: %1$s, ", MapUtils.getInteger(component, "num"));
                configStr += String.format(" cardType: %1$s, ", MapUtils.getInteger(component, "cardType") == 1 ? "'multiple'" : "'single'");
                configStr += String.format(" databaselinkId: '%1$s', ", MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" imgField: '%1$s', ", MapUtils.getString(component, "imgUrl"));
                configStr += String.format(" urlField: '%1$s', ", MapUtils.getString(component, "link"));
                configStr += String.format(" titleField: '%1$s', ", MapUtils.getString(component, "info"));
                configStr += String.format(" tagOneIcon: '%1$s', ", MapUtils.getString(component, "icons1"));
                configStr += String.format(" tagOneText: '%1$s', ", MapUtils.getString(component, "labels1"));
                configStr += String.format(" tagTwoIcon: '%1$s', ", MapUtils.getString(component, "icons2"));
                configStr += String.format(" tagTwoText: '%1$s', ", MapUtils.getString(component, "labels2"));
                configStr += String.format(" keyfield: '%1$s', \r\n", MapUtils.getString(component, "keyfield"));

                configStr += "}, ";

                if (!importStr.contains("xjrImgcard"))
                {
                    importStr += "import xjrImgcard from \"@/components/Img/imgcard.vue\"; \r\n";

                    componentStr += "xjrImgcard,";
                }
            }
            // 图文卡片结束 


            // 视频卡片开始
            if (StringUtil.equalsIgnoreCase(componentType, "videoPlay"))
            {
                code.append("    <xjr-video :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\"  class=\"margin-bottom-sm\"></xjr-video>\r\n");

                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" number: %1$s, ", MapUtils.getString(component, "num"));
                configStr += String.format(" databaselinkId: '%1$s', ", MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" urlField: '%1$s', ", MapUtils.getString(component, "imgUrl"));
                configStr += String.format(" titleField: '%1$s', ", MapUtils.getString(component, "info"));
                configStr += String.format(" tagOneIcon: '%1$s', ", MapUtils.getString(component, "icons1"));
                configStr += String.format(" tagOneText: '%1$s', ", MapUtils.getString(component, "labels1"));
                configStr += String.format(" tagTwoIcon: '%1$s', ", MapUtils.getString(component, "icons2"));
                configStr += String.format(" tagTwoText: '%1$s', ", MapUtils.getString(component, "labels2"));
                configStr += String.format(" keyfield: '%1$s', \r\n", MapUtils.getString(component, "keyfield"));



                configStr += "}, ";

                if (!importStr.contains("xjrVideo"))
                {
                    importStr += "import xjrVideo from \"@/components/yy-video-player/yy-video-player.nvue\"; \r\n";

                    componentStr += "xjrVideo,";
                }

            }
            // 视频卡片结束 



            // 宫格菜单开始
            if (StringUtil.equalsIgnoreCase(componentType, "grid"))
            {
                code.append("    <xjr-grid :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\"  class=\"margin-bottom-sm\"></xjr-grid>\r\n");

                List<Map<String, Object>> menuJobj = (List<Map<String, Object>>) component.get("menu");


                JSONArray menuList = new JSONArray();

                for(Map<String, Object> jt : menuJobj) {
                    JSONObject menu = new JSONObject();
                    menu.put("icon", MapUtils.getString(jt, "F_Icon"));
                    menu.put("url", MapUtils.getString(jt, "F_Url"));
                    menu.put("text", MapUtils.getString(jt, "F_Name"));
                    menu.put("color", MapUtils.getString(jt, ""));
                    menuList.add(menu);
                }

                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" dynamicList: %1$s, ", menuList.toJSONString());
                configStr += "}, ";

                if (!importStr.contains("xjrGrid"))
                {
                    importStr += "import xjrGrid from \"@/components/grid/grid.vue\" \r\n";

                    componentStr += "xjrGrid,";
                }

            }
            // 宫格菜单结束 



            // 列表开始
            if (StringUtil.equalsIgnoreCase(componentType, "List")) {
                code.append("    <xjr-list :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\"  class=\"margin-bottom-sm\"></xjr-list>\r\n");

                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format("        number: %1$s, \r\n", MapUtils.getString(component, "num"));
                configStr += String.format("        title: '%1$s', \r\n", MapUtils.getString(component, "title"));
                configStr += String.format("        showTitle: %1$s, \r\n", MapUtils.getInteger(component, "istitle") == 1 ? "true" : "false");
                configStr += String.format("        showImg: %1$s, \r\n", MapUtils.getInteger(component, "ispic") == 1 ? "true" : "false");
                configStr += String.format("        imgField: '%1$s', \r\n", MapUtils.getString(component, "imgUrl"));
                configStr += String.format("        showMore: %1$s, \r\n", MapUtils.getInteger(component, "ismore") == 1 ? "true" : "false");
                configStr += String.format("        showMoreToUrl: '%1$s', \r\n", MapUtils.getString(component, "moreLink"));
                configStr += String.format("        databaselinkId: '%1$s', \r\n", MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format("        sql: '%1$s', \r\n", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format("        titleField: '%1$s', \r\n", MapUtils.getString(component, "info"));
                configStr += String.format("        subtitleField: '%1$s', \r\n", MapUtils.getString(component, "content"));
                configStr += String.format("        urlField: '%1$s', \r\n", MapUtils.getString(component, "link"));
                configStr += String.format("        keyfield: '%1$s', \r\n", MapUtils.getString(component, "keyfield"));



                configStr += "}, ";

                if (!importStr.contains("xjrList"))
                {

                    importStr += "import xjrList from \"@/components/list/xjrlist.vue\" \r\n";

                    componentStr += "xjrList,";
                }

            }
            // 列表结束 

            // 工作流待办 开始
            if (StringUtil.equalsIgnoreCase(componentType, "taskList")) {
                code.append("    <xjr-up-coming :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\" ></xjr-up-coming>\r\n");


                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" number: %1$s, ", MapUtils.getInteger(component, "num"));
                configStr += "}, ";

                if (!importStr.contains("xjrUpComing"))
                {

                    importStr += "import xjrUpComing from \"@/components/upcoming/XjrUpComing.vue\" \r\n";

                    componentStr += "xjrUpComing,";
                }
            }
            // 工作流待办 结束


            // 表格开始
            if (StringUtil.equalsIgnoreCase(componentType, "table-chart"))
            {
                code.append("    <xjr-table :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\" ></xjr-table>\r\n");

                Map<String, Object> option = MapUtils.getMap(component, "tableOption");


                JSONArray columnList = new JSONArray();
                List<Map<String, Object>> columns = (List<Map<String, Object>>) option.get("column");
                for (Map<String, Object> jt : columns) {
                    JSONObject column = new JSONObject();
                    column.put("title", jt.get("label"));
                    column.put("field", jt.get("prop"));
                    column.put("align", jt.get("align"));
                    column.put("width", jt.get("width"));
                    column.put("count", jt.get("total"));
                    column.put("sum", jt.get("count"));
                    columnList.add(column);
                }


                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" title: '%1$s', ", MapUtils.getString(component, "F_Text"));
                configStr += String.format(" height: %1$s, ", MapUtils.getString(component, "height"));
                configStr += String.format(" databaselinkId: '%1$s', ", component.keySet().stream().anyMatch(key -> StringUtil.equalsIgnoreCase(key, "F_DatabaseLinkId")) ? MapUtils.getString(component, "F_DatabaseLinkId") : MapUtils.getString(component, "F_DataSourceId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" columnList: %1$s, ", columnList.toJSONString());


                configStr += "}, ";


                if (!importStr.contains("xjrTable"))
                {

                    importStr += "import xjrTable from \"@/components/table/xjrtable.vue\" \r\n";


                    componentStr += "xjrTable,";
                }

            }
            // 表格结束 


            // 折线图开始
            if (StringUtil.equalsIgnoreCase(componentType, "line-chart"))
            {
                code.append("    <xjr-line :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\" class=\"margin-bottom-sm\" ></xjr-line>\r\n");


                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" title: '%1$s', ", MapUtils.getString(component, "F_Text"));
                configStr += String.format(" subtitle: '%1$s', ", MapUtils.getString(component, "F_Subtext"));
                configStr += String.format(" refreshTime: %1$s, ", MapUtils.getString(component, "F_RefreshTime"));
                configStr += String.format(" height: '%1$s', ", MapUtils.getString(component, "height"));
                configStr += String.format(" databaselinkId: '%1$s', ", MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" categoryField: '%1$s', ", MapUtils.getString(component, "F_Category"));
                configStr += String.format(" xField: '%1$s', ", MapUtils.getString(component, "F_Name"));
                configStr += String.format(" yField: '%1$s', ", MapUtils.getString(component, "F_Value"));



                configStr += "}, ";

                if (!importStr.contains("xjrLine"))
                {
                    importStr += "import xjrLine from \"@/components/chart/line/xjrline.vue\" \r\n";


                    componentStr += "xjrLine,";
                }

            }
            // 折线图结束 


            // 柱状图开始
            if (StringUtil.equalsIgnoreCase(componentType, "bar-chart"))
            {
                code.append("    <xjr-bar :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\" class=\"margin-bottom-sm\" ></xjr-bar>\r\n");


                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" title: '%1$s', ", MapUtils.getString(component, "F_Text"));
                configStr += String.format(" subtitle: '%1$s', ", MapUtils.getString(component, "F_Subtext"));
                configStr += String.format(" refreshTime: %1$s, ", MapUtils.getString(component, "F_RefreshTime"));
                configStr += String.format(" height: '%1$s', ", MapUtils.getString(component, "height"));
                configStr += String.format(" databaselinkId: '%1$s', ", MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" categoryField: '%1$s', ", MapUtils.getString(component, "F_Category"));
                configStr += String.format(" xField: '%1$s', ", MapUtils.getString(component, "F_Name"));
                configStr += String.format(" yField: '%1$s', ", MapUtils.getString(component, "F_Value"));



                configStr += "}, ";

                if (!importStr.contains("xjrBar"))
                {

                    importStr += "import xjrBar from \"@/components/chart/bar/xjrbar.vue\" \r\n";

                    componentStr += "xjrBar,";
                }

            }
            // 柱状图结束


            // 条形堆叠图开始
            if (StringUtil.equalsIgnoreCase(componentType, "verticalBar-chart"))
            {
                code.append("    <xjr-stack :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\" class=\"margin-bottom-sm\" ></xjr-stack>\r\n");


                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" title: '%1$s', ", MapUtils.getString(component, "F_Text"));
                configStr += String.format(" subtitle: '%1$s', ", MapUtils.getString(component, "F_Subtext"));
                configStr += String.format(" refreshTime: %1$s, ", MapUtils.getString(component, "F_RefreshTime"));
                configStr += String.format(" height: '%1$s', ", MapUtils.getString(component, "height"));
                configStr += String.format(" databaselinkId: '%1$s', ", MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" categoryField: '%1$s', ", MapUtils.getString(component, "F_Category"));
                configStr += String.format(" xField: '%1$s', ", MapUtils.getString(component, "F_Name"));
                configStr += String.format(" yField: '%1$s', ", MapUtils.getString(component, "F_Value"));



                configStr += "}, ";

                if (!importStr.contains("xjrStack"))
                {

                    importStr += "import xjrStack from \"@/components/chart/stack/xjrstack.vue\" \r\n";

                    componentStr += "xjrStack,";
                }

            }
            // 条形堆叠图结束


            // 饼图开始
            if (StringUtil.equalsIgnoreCase(componentType, "pie-chart"))
            {
                code.append("    <xjr-pie :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\" class=\"margin-bottom-sm\" ></xjr-pie>\r\n");


                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" title: '%1$s', ", MapUtils.getString(component, "F_Text"));
                configStr += String.format(" subtitle: '%1$s', ", MapUtils.getString(component, "F_Subtext"));
                configStr += String.format(" refreshTime: %1$s, ", MapUtils.getString(component, "F_RefreshTime"));
                configStr += String.format(" height: '%1$s', ", MapUtils.getString(component, "height"));
                configStr += String.format(" databaselinkId: '%1$s', ", MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" categoryField: '%1$s', ", MapUtils.getString(component, "F_Category"));
                configStr += String.format(" yField: '%1$s', ", MapUtils.getString(component, "F_Value"));



                configStr += "}, ";

                if (!importStr.contains("xjrPie"))
                {

                    importStr += "import xjrPie from \"@/components/chart/pie/xjrpie.vue\" \r\n";


                    componentStr += "xjrPie,";
                }

            }
            // 饼图结束

            // 环形图开始
            if (StringUtil.equalsIgnoreCase(componentType, "circle-chart"))
            {
                code.append("    <xjr-ring :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\"  class=\"margin-bottom-sm\"></xjr-ring>\r\n");


                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" title: '%1$s', ", MapUtils.getString(component, "F_Text"));
                configStr += String.format(" subtitle: '%1$s', ", MapUtils.getString(component, "F_Subtext"));
                configStr += String.format(" refreshTime: %1$s, ", MapUtils.getString(component, "F_RefreshTime"));
                configStr += String.format(" height: '%1$s', ", MapUtils.getString(component, "height"));
                configStr += String.format(" databaselinkId: '%1$s', ", MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" categoryField: '%1$s', ", MapUtils.getString(component, "F_Category"));
                configStr += String.format(" yField: '%1$s', ", MapUtils.getString(component, "F_Value"));



                configStr += "}, ";


                if (!importStr.contains("xjrRing"))
                {
                    importStr += "import xjrRing from \"@/components/chart/ring/xjrring.vue\" \r\n";

                    componentStr += "xjrRing,";
                }

            }
            // 环形图结束

            // 堆叠面积图开始
            if (StringUtil.equalsIgnoreCase(componentType, "area-chart"))
            {
                code.append("    <xjr-area :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\" class=\"margin-bottom-sm\" ></xjr-area>\r\n");


                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" title: '%1$s', ", MapUtils.getString(component, "F_Text"));
                configStr += String.format(" subtitle: '%1$s', ", MapUtils.getString(component, "F_Subtext"));
                configStr += String.format(" refreshTime: %1$s, ", MapUtils.getString(component, "F_RefreshTime"));
                configStr += String.format(" height: '%1$s', ", MapUtils.getString(component, "height"));
                configStr += String.format(" databaselinkId: '%1$s', ", MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" categoryField: '%1$s', ", MapUtils.getString(component, "F_Category"));
                configStr += String.format(" xField: '%1$s', ", MapUtils.getString(component, "F_Name"));
                configStr += String.format(" yField: '%1$s', ", MapUtils.getString(component, "F_Value"));



                configStr += "}, ";
                if (!importStr.contains("xjrArea"))
                {

                    importStr += "import xjrArea from \"@/components/chart/area/xjrarea.vue\" \r\n";

                    componentStr += "xjrArea,";
                }


            }
            // 堆叠面积图结束




            // 漏斗图开始
            if (StringUtil.equalsIgnoreCase(componentType, "funnel-chart")) {
                code.append("    <xjr-funnel :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\" class=\"margin-bottom-sm\" ></xjr-funnel>\r\n");


                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" title: '%1$s', ", MapUtils.getString(component, "F_Text"));
                configStr += String.format(" subtitle: '%1$s', ", MapUtils.getString(component, "F_Subtext"));
                configStr += String.format(" refreshTime: %1$s, ", MapUtils.getString(component, "F_RefreshTime"));
                configStr += String.format(" height: '%1$s', ", MapUtils.getString(component, "height"));
                configStr += String.format(" databaselinkId: '%1$s', ", MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" categoryField: '%1$s', ", MapUtils.getString(component, "F_Category"));
                configStr += String.format(" yField: '%1$s', ", MapUtils.getString(component, "F_Value"));



                configStr += "}, ";

                if (!importStr.contains("xjrFunnel"))
                {
                    importStr += "import xjrFunnel from \"@/components/chart/funnel/xjrfunnel.vue\" \r\n";

                    componentStr += "xjrFunnel,";
                }

            }
            // 漏斗图结束

            //堆叠柱状图
            if (StringUtil.equalsIgnoreCase(componentType, "pileBar-chart")) {
                code.append("    <xjr-bar-stack :componentConfig=\"" + MapUtils.getString(component, "id") + "Config\" class=\"margin-bottom-sm\" ></xjr-bar-stack>\r\n");


                configStr += MapUtils.getString(component, "id") + "Config : {  ";
                configStr += String.format(" title: '%1$s', ", MapUtils.getString(component, "F_Text"));
                configStr += String.format(" subtitle: '%1$s', ", MapUtils.getString(component, "F_Subtext"));
                configStr += String.format(" refreshTime: %1$s, ", MapUtils.getString(component, "F_RefreshTime"));
                configStr += String.format(" height: '%1$s', ", MapUtils.getString(component, "height"));
                configStr += String.format(" databaselinkId: '%1$s', ", MapUtils.getString(component, "F_DatabaseLinkId"));
                configStr += String.format(" sql: '%1$s', ", MapUtils.getString(component, "sql").replace("\r\n", ""));
                configStr += String.format(" categoryField: '%1$s', ", MapUtils.getString(component, "F_Category"));
                configStr += String.format(" xField: '%1$s', ", MapUtils.getString(component, "F_Name"));
                configStr += String.format(" yField: '%1$s', ", MapUtils.getString(component, "F_Value"));



                configStr += "}, ";

                if (!importStr.contains("xjrBarStack"))
                {
                    importStr += "import xjrBarStack from \"@/components/chart/bar/xjrbarStack.vue\" \r\n";

                    componentStr += "xjrBarStack,";
                }
            }
            //堆叠柱状图结束
        }



        code.append("    </view>\r\n");
        code.append("</template>\r\n");

        code.append("<script>\r\n");

        code.append(importStr);

        code.append("export default {\r\n");
        code.append("    name:'index',\r\n");
        code.append("    data(){\r\n");
        code.append("        return {" + configStr + "}\r\n");
        code.append("    },\r\n");
        code.append("   components:{" + componentStr + "},\r\n");

        code.append("}\r\n");

        code.append("</script>\r\n");

        code.append("<style scoped>\r\n");
        code.append(".phone-content {\r\n");
        code.append("   height: 100vh;\r\n");
        code.append("}\r\n");

        code.append(".page {\r\n");
        code.append("   padding: 10px;\r\n");
        code.append("}\r\n");

        code.append("</style>\r\n");


        return code.toString();
    }
}
