package com.xjrsoft.module.base.utils;

import com.xjrsoft.core.tool.utils.StringUtil;
import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Copyright 2016-2019 长沙湘北智造信息技术有限公司
 *
 * @version xbzz V2.0.28
 * @author 湘北智造-框架开发组
 * @date：2019年11月6日
 * @description：
 *		拼音工具类
 */
public class PinYinUtil {

	/**
	 * @author 湘北智造-框架开发组
	 * @date：2019年11月6日
	 * @description：获取汉字简拼
	 * @param str	汉字字符串
	 * @return
	 */
	public static String getPinYinHeaderChar(String str) {
		StringBuilder result = new StringBuilder();
		if (!StringUtil.isEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {
				char word = str.charAt(i);
				String[] pinYinArray = PinyinHelper.toHanyuPinyinStringArray(word);
				if (pinYinArray != null) {
					result.append(pinYinArray[0].charAt(0));
				} else {
					// 不是汉字
					result.append(word);
				}
			}
		}
		return result.toString();
	}
}