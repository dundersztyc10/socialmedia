package pl.dundersztyc.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy
class EventListenerLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(EventListenerLoggingAspect.class);

    @Pointcut("@annotation(org.springframework.context.event.EventListener)")
    private void anyEventListener() {
    }

    @Before("anyEventListener()")
    public void beforeAnyEventListener(JoinPoint joinPoint) {
        log.info(":: BEFORE :: " + joinPoint.getSignature().getName());
    }


}
