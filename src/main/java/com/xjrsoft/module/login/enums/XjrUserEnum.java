package com.xjrsoft.module.login.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum XjrUserEnum {

	/**
	 * web
	 */
	WEB("web", 1),

	/**
	 * app
	 */
	APP("app", 2),
	;

	final String name;
	final int category;

}