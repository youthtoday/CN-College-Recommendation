package com.li.aop;

import com.alibaba.fastjson.JSON;
import com.li.entity.Syslog;
import com.li.service.SyslogService;
import com.li.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: LogAopAspect
 * @Description: AOP实现日志  aop环绕通知类
 * @Authror: XQD
 * @Date: 2021/3/9 10:36
 */
@Order(3)  // 标记支持AspectJ的切面排序
@Component
@Aspect
public class LogAopAspect{

    @Autowired
    private SyslogService syslogService;

    @Pointcut("@annotation(com.li.aop.LogAnno)")
    public void operLogPoinCut() {

    }

    @AfterReturning(value = "operLogPoinCut()", returning = "keys")
    public Object aroundAdvice(JoinPoint joinPoint, Object keys) {

        // 1.方法执行前的处理，相当于前置通知
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取方法
        Method method = methodSignature.getMethod();
        // 获取方法上面的注解
        LogAnno logAnno = method.getAnnotation(LogAnno.class);
        // 获取操作描述的属性值
        String operateType = logAnno.operateType();
        Syslog syslog = new Syslog();


        String className = joinPoint.getTarget().getClass().getName();
        String signatureName = methodSignature.getName();
        //获取uid
        String userid;
        String token = ServletUtils.getRequest().getHeader("token");
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
            syslog.setUid(Integer.parseInt(userid));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //存请求参数
        String requestMethod = ServletUtils.getRequest().getMethod();
        if ("PUT".equals(requestMethod) || "POST".equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            System.out.println("POST:"+params);
            syslog.setParams(StringUtils.substring(params, 0, 2000));
        } else {
            // 获取RequestAttributes
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            // 从获取RequestAttributes中获取HttpServletRequest的信息
            HttpServletRequest request = (HttpServletRequest) requestAttributes
                    .resolveReference(RequestAttributes.REFERENCE_REQUEST);
            //获取pathVariabe
            Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            String sid = (String)pathVariables.get("id");
            // 请求的参数
            System.out.println("GET:"+sid);
            syslog.setSid(Integer.parseInt(sid));
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            syslog.setParams(StringUtils.substring(JSON.toJSONString(rtnMap), 0, 2000));
        }
//        // 接口返回值参数
//        adminOperLog.setOperateResult(JSON.toJSONString(keys));
//        adminOperLog.setOperateType(operateType);
//        String ip = HttpContextUtil.getIpAddress();
//        adminOperLog.setOperateIp(ip);
        syslog.setOperation(operateType);
        syslog.setCreateTime(new Date());
        Object result = null;
        syslogService.saveLog(syslog);// 添加日志记录
        return result;
    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    if (jsonObj != null) {
                        params += jsonObj.toString() + " ";
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}

