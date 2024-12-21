package com.ev.ocpp16.global.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
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

    @Before("execution(* com.ev.ocpp16.websocket..ActionHandler+.handleAction(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        String shortString = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("====> [{}] args: {}", shortString, args);
    }

    @AfterReturning(pointcut = "execution(* com.ev.ocpp16.websocket..ActionHandler+.handleAction(..))", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        String shortString = joinPoint.getSignature().toShortString();
        log.info("<==== [{}] return: {}", shortString, result);
    }
}