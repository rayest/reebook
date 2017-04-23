package cn.reebook.foundation.aspect;

import cn.reebook.foundation.exception.BusinessException;
import cn.reebook.foundation.exception.InvalidParamsException;
import cn.reebook.foundation.security.Constants;
import org.apache.catalina.connector.RequestFacade;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Aspect
@Component
@ConfigurationProperties(prefix = "security")
public class ValidateAspect {
    private List<String> whiteList = new ArrayList<>();

    public List<String> getWhiteList() {
        return whiteList;
    }

    @Before("execution(* *(@org.springframework.web.bind.annotation.RequestBody *,..))||execution(* *(@org.springframework.web.bind.annotation.RequestBody (*),..,..))")
    public void validateBodyParams(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null && !(arg instanceof String) && !(arg instanceof Integer)) {
                Set<ConstraintViolation<Object>> constraintViolations = getValidator().validate(arg);
                if (constraintViolations.size() > 0) {
                    throw new InvalidParamsException(constraintViolations.iterator().next().getMessage());
                }
            }
        }
    }

    @Before("@annotation(cn.reebook.foundation.aspect.ValidateParams)")
    public void validateQueryParams(JoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object[] parameterValues = joinPoint.getArgs();

        Set<ConstraintViolation<Object>> violations = getMethodValidator().validateParameters(joinPoint.getTarget(), method, parameterValues);
        if (!violations.isEmpty()) {
            throw new InvalidParamsException(violations.iterator().next().getMessage());
        }
    }

    //检查头部的userName和路径中的userName是否一致,如果不一致,则数据已经被窜改
    @Before("within(@org.springframework.web.bind.annotation.RestController *) && execution(* *(..))")
    public void advice(JoinPoint thisJoinPoint) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        RequestFacade requestFacade = (RequestFacade) servletRequestAttributes.getRequest();
        String userNameInHeader = requestFacade.getHeader("userName");
        if (userNameInHeader == null) {
            return;
        }
        String uri = requestFacade.getRequestURI();
        if (isFreeSecurity(uri)) {
            return;
        }
        final Signature signature = thisJoinPoint.getStaticPart().getSignature();

        if (signature instanceof MethodSignature) {
            final MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            Annotation[][] annotations = method.getParameterAnnotations();
            if (method.getName().indexOf("loadAvatar") > -1) {
                int a = 0;
            }
            for (int i = 0; i < annotations.length; i++) {
                if (annotations[i].length > 0 && annotations[i][0] instanceof PathVariable) {
                    String paramName = ((PathVariable) annotations[i][0]).value();
                    if (paramName != null && paramName.equals("userName")) {
                        Object paramValue = thisJoinPoint.getArgs()[i];
                        if (!userNameInHeader.equals(paramValue)) {
                            throw new BusinessException(Constants.SECURITY_USERNAME_IN_HEADER_IS_NOT_CONSISTENT_WITH_PATH);
                        }
                    }
                }
            }
        }
    }

    private ExecutableValidator getMethodValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator().forExecutables();
    }

    private Validator getValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    private boolean isFreeSecurity(String uri) {
        for (String freeSecurityUrl : whiteList) {
            UriTemplate uriTemplate = new UriTemplate(freeSecurityUrl);
            if (uriTemplate.matches(uri)) {
                return true;
            }
        }
        return false;
    }
}
