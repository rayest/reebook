package cn.reebook.foundation.aspect;

import cn.reebook.foundation.common.SequenceNumber;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Service;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xubt on 5/18/16.
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class})})
@Service
public class DBInterceptor implements Interceptor {

    @Resource
    private SequenceNumber sequenceNumber;

    public static boolean isJavaClass(Class<?> clazz) {
        return clazz != null && clazz.getClassLoader() == null;
    }

    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement stmt = (MappedStatement) invocation.getArgs()[0];
        Object entityToSave = invocation.getArgs()[1];
        if (stmt == null) return invocation.proceed();
        if (stmt.getSqlCommandType().equals(SqlCommandType.INSERT)) {
            if (entityToSave instanceof Map) {
                ((Map) entityToSave).values().stream().filter(param -> !isJavaClass(param.getClass())).forEach(param -> {
                    if (ReflectionTestUtils.getField(param, "id") == null) {
                        ReflectionTestUtils.setField(param, "id", sequenceNumber.generate());
                    }
                });
            } else {
                if (ReflectionTestUtils.getField(entityToSave, "id") == null) {
                    ReflectionTestUtils.setField(entityToSave, "id", sequenceNumber.generate());
                }
            }
        }
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }
}
