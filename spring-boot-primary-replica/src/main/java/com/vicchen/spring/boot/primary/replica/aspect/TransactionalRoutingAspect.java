package com.vicchen.spring.boot.primary.replica.aspect;

import com.vicchen.spring.boot.primary.replica.config.DataSourceContextHolder;
import com.vicchen.spring.boot.primary.replica.config.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Aspect
@Component
public class TransactionalRoutingAspect {

    @Pointcut("@annotation(tx)")
    public void txPointCut(Transactional tx) {}

    @Before(value = "txPointCut(tx)", argNames = "joinPoint,tx")
    public void beforeTransaction(JoinPoint joinPoint, Transactional tx) {
        String methodName = joinPoint.getSignature().toShortString();
        
        if (tx != null && tx.readOnly()) {
            DataSourceContextHolder.set(DataSourceType.REPLICA);
            log.info("📖 [TRANSACTION] {} -> READ-ONLY -> Route to REPLICA", methodName);
        } else {
            DataSourceContextHolder.set(DataSourceType.PRIMARY);
            log.info("✏️ [TRANSACTION] {} -> WRITE -> Route to PRIMARY", methodName);
        }
    }

    @After(value = "txPointCut(tx)", argNames = "joinPoint,tx")
    public void afterTransaction(JoinPoint joinPoint, Transactional tx) {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("🧹 [TRANSACTION] {} -> Context cleared", methodName);
        DataSourceContextHolder.clear();
    }
}
