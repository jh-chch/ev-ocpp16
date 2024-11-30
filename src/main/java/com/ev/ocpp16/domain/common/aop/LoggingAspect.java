package com.ev.ocpp16.domain.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component("domainLoggingAspect")
public class LoggingAspect {
    @Before("execution(* com.ev.ocpp16.domain..service..*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {
        String shortString = joinPoint.getSignature().toShortString();
        log.info("====> [{}]", shortString);
    }
}