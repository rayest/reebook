package cn.reebook.foundation.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bobo on 17/3/8.
 */
@Aspect
@Component
@ConditionalOnProperty("performanceMonitor.enabled")
public class PerformanceMonitorAspect {
    private static ConcurrentHashMap<String, MethodStats> methodStats = new ConcurrentHashMap<String, MethodStats>();
    private static long statLogFrequency = 10;
    private static long methodWarningThreshold = 1000;
    Logger logger = LoggerFactory.getLogger(PerformanceMonitorAspect.class);

    @Pointcut("execution(*  cn.reebook..*.*(..))")
    private void pointMethod() {
    }

    @Around("pointMethod()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return pjp.proceed();
        }
        finally{
                updateStats(pjp.getTarget().getClass().getName() + "." + pjp.getSignature().getName(), (System.currentTimeMillis() - start));
            }
    }

    private void updateStats(String methodName, long elapsedTime) {
        MethodStats stats = methodStats.get(methodName);
        if(stats == null) {
            stats = new MethodStats(methodName);
            methodStats.put(methodName,stats);
        }
        stats.count++;
        stats.totalTime += elapsedTime;
        if(elapsedTime > stats.maxTime) {
            stats.maxTime = elapsedTime;
        }

        if(elapsedTime > methodWarningThreshold) {
            logger.warn("method warning: " + methodName + "(), cnt = " + stats.count + ", lastTime = " + elapsedTime + ", maxTime = " + stats.maxTime);
        }

        if(stats.count % statLogFrequency == 0) {
            long avgTime = stats.totalTime / stats.count;
            long runningAvg = (stats.totalTime-stats.lastTotalTime) / statLogFrequency;
            logger.debug("method: " + methodName + "(), cnt = " + stats.count + ", lastTime = " + elapsedTime + ", avgTime = " + avgTime + ", runningAvg = " + runningAvg + ", maxTime = " + stats.maxTime);
            //reset the last total time
            stats.lastTotalTime = stats.totalTime;
        }
    }

    class MethodStats {
        public String methodName;
        public long count;
        public long totalTime;
        public long lastTotalTime;
        public long maxTime;

        public MethodStats(String methodName) {
            this.methodName = methodName;
        }
    }
}
