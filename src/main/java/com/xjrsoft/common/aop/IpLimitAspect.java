package com.xjrsoft.common.aop;

import com.xjrsoft.common.result.Response;
import com.xjrsoft.common.result.ResultCode;
import com.xjrsoft.common.utils.IpUtil;
import com.xjrsoft.common.utils.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @desc: Ip限流切面
 * @author: Tzx
 **/
@Aspect
@Component
@Slf4j
public class IpLimitAspect {

    @Autowired
    private RedisUtil redisUtil; //redis 操作类

    @Value("${xjrsoft.global-config.ipLimitConfig.enabled}")
    private Boolean enabled;

    @Value("${xjrsoft.global-config.ipLimitConfig.whiteIpList}")
    private List<String> whiteIpList;

    @Value("${xjrsoft.global-config.ipLimitConfig.whiteApiList}")
    private List<String> whiteApiList;

    @Value("${xjrsoft.global-config.ipLimitConfig.time}")
    private long time;

    @Value("${xjrsoft.global-config.ipLimitConfig.hits}")
    private Integer hits;



    /**
     * 定义切入点，切入点为com.xjrsoft.module.*.controller
     *通过@Pointcut注解声明频繁使用的切点表达式
     */
    @Pointcut("execution(public * com.xjrsoft.module.*.controller..*(..)))")
    public void ipLimitAspect(){

    }

    /**
     * @description  环绕
     */
    @Around("ipLimitAspect()")
    @SneakyThrows
    public Object doAroundGame(ProceedingJoinPoint joinPoint){
        //TODO
        if(!enabled){
            return joinPoint.proceed();
        }


        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        String ip = IpUtil.getIpAddr(request);
        String url = request.getRequestURI();

        String key = ip + "+" + url;


        //判断当前ip 是否存在于白名单
        if(whiteIpList.contains(ip)){
           return joinPoint.proceed();
        }

        //判断当前url  是否存在于白名单
        if(whiteApiList.contains(url)){
            return joinPoint.proceed();
        }

        //判断是否当前key
        if(redisUtil.hasKey(key)){
            Integer cacheHits = (Integer) redisUtil.get(key);
            //如果次数小于 设定的值  则继续执行
            if(cacheHits < hits){
                Object result =  joinPoint.proceed(); //执行方法
                redisUtil.incr(key,1);//将缓存的值 自增
                return  result;
            }
            else {//如果次数已经大于等于 设定的值 需要提示错误
                return Response.notOk(ResultCode.REQ_REJECT.getCode(),"访问过于频繁，请稍后重试！");
            }
        }
        else { //如果没有这个key  则可以继续执行  并且再次给他加入到缓存中。
            Object result = joinPoint.proceed(); //执行方法
            redisUtil.set(key,1,time);
            return  result;
        }
    }

}
