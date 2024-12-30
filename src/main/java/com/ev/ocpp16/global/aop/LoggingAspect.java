package com.ev.ocpp16.global.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    private final HttpServletRequest httpServletRequest;

    public LoggingAspect(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    // CONTROLLE
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {
    }

    @Around("restControllerMethods()")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestId = UUID.randomUUID().toString();
        MDC.put("mdcKey", requestId);

        // 요청 경로와 파라미터 추출
        String requestUrl = httpServletRequest.getRequestURI();
        String queryString = httpServletRequest.getQueryString();

        // 메서드 및 매개변수 정보 추출
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();

        // 요청 로그
        log.info("<<<<<<<<<< Request: URL = {}?{} | Method = {} | Arguments = {} >>>>>>>>>>",
                requestUrl, queryString, method.getName(), Arrays.toString(args));

        try {
            Object result = joinPoint.proceed();

            // 응답 로그
            log.info("<<<<<<<<<< Response: URL = {} | Method = {} | Result = {} >>>>>>>>>>",
                    requestUrl, method.getName(), result);

            return result;
        } catch (Exception e) {
            log.error("<<<<<<<<<< Error in method: {} | URL = {} | Exception = {} >>>>>>>>>>",
                    method.getName(), requestUrl, e.getMessage());
            throw e;
        }
    }
    // CONTROLLE

    @Before("execution(* com.ev.ocpp16.domain..service..*(..))")
    public void domainLogMethodEntry(JoinPoint joinPoint) {
        String shortString = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("====> [{}] args: {}", shortString, args);
    }

    @Before("execution(* com.ev.ocpp16.application..*(..))")
    public void applicationLogMethodEntry(JoinPoint joinPoint) {
        String shortString = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("====> [{}] args: {}", shortString, args);
    }

    // WEBSOCKET
    @Before("execution(* com.ev.ocpp16.websocket..ActionHandler+.handleAction(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        String shortString = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("<<<<<<<<<< {} START >>>>>>>>>>", shortString);
        log.info("====> [{}] args: {}", shortString, args);
    }

    @AfterReturning(pointcut = "execution(* com.ev.ocpp16.websocket..ActionHandler+.handleAction(..))", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        String shortString = joinPoint.getSignature().toShortString();
        log.info("<==== [{}] return: {}", shortString, result);
        log.info("<<<<<<<<<< {} END >>>>>>>>>>", shortString);
    }
    // WEBSOCKET
}