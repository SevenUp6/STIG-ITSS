package com.xjrsoft.common.aop;

import com.alibaba.fastjson.JSONObject;
import com.xjrsoft.common.allenum.LogCategoryEnum;
import com.xjrsoft.common.allenum.OperateTypeEnum;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.utils.IpUtil;
import com.xjrsoft.core.secure.utils.SecureUtil;
import com.xjrsoft.core.tool.utils.StringPool;
import com.xjrsoft.core.tool.utils.StringUtil;
import com.xjrsoft.core.tool.utils.WebUtil;
import com.xjrsoft.module.base.entity.XjrBaseLog;
import com.xjrsoft.module.base.service.IXjrBaseLogService;
import com.xjrsoft.module.base.utils.LogUtil;
import com.xjrsoft.module.login.dto.LoginInfoDto;
import com.xjrsoft.module.login.vo.LoginSuccessVo;
import com.xjrsoft.module.login.vo.UserInfoVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @desc: 日志切面
 * @author: CSH
 **/
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private IXjrBaseLogService logService;

    @Value("${xjrsoft.global-config.logblackApiList}")
    private String logblackApiList;

    /**
     * 定义切入点，切入点为com.xjrsoft.module.*.controller
     *通过@Pointcut注解声明频繁使用的切点表达式
     */
//    @Pointcut("execution(public * com.xjrsoft.module.base.controller.XjrBaseCompanyController.*(..)))")
    @Pointcut("execution(public * com.xjrsoft.module.*.controller..*(..)))")
    public void LogAspect(){

    }

    /**
     * @description  在连接点执行之前执行的通知
     */
    @Before("LogAspect()")
    public void doBeforeGame(JoinPoint joinPoint){
        log.info("执行方法之前");
        //TODO
    }

    /**
     * @description  在连接点执行之后执行的通知（返回通知和异常通知的异常）
     */
    @AfterReturning(value = "LogAspect()",returning = "result")
    public void doAfterGame(JoinPoint joinPoint,Object result) throws Throwable {
        log.info("执行方法之后");
        //TODO
        // 获取RequestAttributes
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return;
        }
        String url = request.getRequestURI();
        String[] split = logblackApiList.split(",");
        for (String s : split) {
            if(url.contains(s)) {
                return;
            }
        }


//        Object result = joinPoint.();

        // 获取请求参数进行打印
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        // swagger中文注释名
        ApiOperation annotation = methodSignature.getMethod().getAnnotation(ApiOperation.class);
        String methodName = StringPool.EMPTY;
        if (annotation != null) {
            String methodCommentName = annotation.value();
            methodName = signature.getName() + "[" + methodCommentName + "]";
        }


        XjrBaseLog log = new XjrBaseLog();

        assert request != null;
        log.setCategoryId(getCategory(request));
        log.setHost(request.getRemoteHost());
        log.setOperateTime(LocalDateTime.now());
        log.setIpAddress(IpUtil.getIpAddr(request));
        log.setOperateType(LogUtil.getOperateType(request).getValue());
        log.setOperateTypeId(Integer.toString(LogUtil.getOperateType(request).getCode()));
        log.setExecuteResult(1);
        log.setExecuteResultJson(JSONObject.toJSONString(result));
        log.setBrowser(LogUtil.getBrowser((request.getHeader("user-agent"))));
        log.setModule(methodName);
        if(StringUtils.startsWithIgnoreCase(request.getRequestURI(), "/login")) {
            Response<LoginSuccessVo> response = (Response<LoginSuccessVo>) result;
            LoginSuccessVo loginSuccessVo = response.getData();
            if (loginSuccessVo != null) {
                UserInfoVo userInfoVo = loginSuccessVo.getUserInfoVo();
                log.setOperateAccount(userInfoVo.getAccount());
                log.setOperateUserId(userInfoVo.getUserId());
            } else {
                log.setExecuteResult(-1);
            }
        } else {
            log.setOperateAccount(SecureUtil.getUserAccount());
            log.setOperateUserId(SecureUtil.getUserId());
        }
        logService.addLog(log);

    }

    /**
     * @title 获取日志分类
     * @create 2020年11月11日 19:23:30
     * */
    private int getCategory(HttpServletRequest request){
        if(request.getRequestURI().equals("/login")){
            return LogCategoryEnum.LOGIN.getCode();
        }
        else if (request.getMethod().toLowerCase().equals("get")){
            return  LogCategoryEnum.GET.getCode();
        }
        else if(request.getMethod().toLowerCase().equals("post") || request.getMethod().toLowerCase().equals("put") || request.getMethod().toLowerCase().equals("patch") || request.getMethod().toLowerCase().equals("delete")) {
            return  LogCategoryEnum.OPERAT.getCode();
        }
        else
            return  LogCategoryEnum.EXCEPTION.getCode();
    }
}
