package com.xjrsoft.module.login.utils;

import com.xjrsoft.core.tool.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsKit {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmsKit.class);

	/**
	 * 通过调用华为云的短信api发送验证码
	 *
	 * @param mobile
	 * @return
	 */
	public static String sendSmsByHuaWei(String mobile) {
		String code = getRandomNum(6);
		SmsHuaWei smsHuaWei = SpringUtil.getBean(SmsHuaWei.class);
		String sms = smsHuaWei.sendSmsSimple(mobile, code);
		LOGGER.info("Send Sms: " + sms);
		return code;
	}

	public static String getRandomNum(int n) {
		String val = "";
		Random random = new Random();
		for ( int i = 0; i < n; i++ ) {
			// 产生数字
			val += String.valueOf( random.nextInt( 10 ) );
		}
		return val;
	}

	/**
	 * 验证手机号码是否符合要求 中国电信号段 133、149、153、173、177、180、181、189、199 中国联通号段
	 * 130、131、132、145、155、156、166、175、176、185、186 中国移动号段
	 * 134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198
	 * 其他号段 14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。 虚拟运营商 电信：1700、1701、1702
	 * 移动：1703、1705、1706 联通：1704、1707、1708、1709、171 卫星通信：1349
	 *
	 * @param phone
	 */
	public static boolean isPhone(String phone) {
		boolean result = false;
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
		if (phone.length() != 11) {
			result = false;// false
		} else {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(phone);
			boolean isMatch = m.matches();
			if (!isMatch) {
				result = false;// false
			} else {
				result = true;// true
			}
		}
		return result;
	}
	
	/**
	 * 通过调用华为云的短信api发送消息通知
	 *
	 * @param mobile
	 * @return
	 */
	public static void sendMessageByHuaWei(String mobile,String message) {
		SmsHuaWei smsHuaWei = SpringUtil.getBean(SmsHuaWei.class);
		String [] messages=message.split(",");
		String sms = smsHuaWei.sendMessageByHuaWei(mobile, messages[0]);
		System.out.println(sms);
		LOGGER.info("Send Sms: " + sms);
	}

}
