package com.xjrsoft.common.handler;

import com.alibaba.fastjson.JSONObject;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.result.ResultCode;
import com.xjrsoft.common.utils.IpUtil;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.WebUtil;
import com.xjrsoft.module.base.entity.XjrBaseLog;
import com.xjrsoft.module.base.service.IXjrBaseLogService;
import com.xjrsoft.module.base.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

	@Value("${xjrsoft.global-config.exceptionEnabled}")
	private boolean exceptionEnabled;

	@Autowired
	private IXjrBaseLogService logService;

	@ResponseBody
    @ExceptionHandler(value =Exception.class)
	public Response exceptionHandler(Exception e){
		String errMsg = e.getMessage() == null ? e.toString() : e.getMessage();
		log.error("内部服务器错误！", e);
		// 保存异常日志
		XjrBaseLog log = buildLog(errMsg);
		logService.addLog(log);
		if(exceptionEnabled){
			return Response.notOk(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "内部服务器错误，请联系系统管理员。");
		}

       	return Response.notOk(ResultCode.INTERNAL_SERVER_ERROR.getCode(), errMsg);
    }

    private XjrBaseLog buildLog(String errMsg) {
		HttpServletRequest request = WebUtil.getRequest();
		XjrBaseLog log = new XjrBaseLog();

		assert request != null;
		log.setCategoryId(4);
		log.setHost(request.getRemoteHost());
		log.setOperateAccount(SecureUtil.getUserAccount());
		log.setOperateTime(LocalDateTime.now());
		log.setOperateUserId(SecureUtil.getUserId());
		log.setIpAddress(IpUtil.getIpAddr(request));
		log.setOperateType(LogUtil.getOperateType(request).getValue());
		log.setOperateTypeId(Integer.toString(LogUtil.getOperateType(request).getCode()));
		log.setExecuteResult(-1);
		log.setExecuteResultJson(errMsg);
		log.setBrowser(LogUtil.getBrowser((request.getHeader("user-agent"))));
//		log.setModule(methodName);
		return log;
	}

}