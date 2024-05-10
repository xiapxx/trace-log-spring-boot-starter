package io.github.xiapxx.starter.tracelog.core.controllerlog;

import io.github.xiapxx.starter.tracelog.annotation.IgnoreLog;
import io.github.xiapxx.starter.tracelog.core.ParamTransfer;
import io.github.xiapxx.starter.tracelog.interfaces.ControllerLogCustomizer;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author xiapeng
 * @Date 2024-04-22 17:43
 */
public class ControllerLogPointcutAdvisor extends AbstractPointcutAdvisor {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher(".");

    private List<String> basePackageList;

    private Pointcut pointcut;

    private Advice advice;

    @Autowired
    private ParamTransfer paramTransfer;

    @Autowired
    private ObjectProvider<ControllerLogCustomizer> controllerLogCustomizerObjectProvider;

    public ControllerLogPointcutAdvisor(String[] basePackages){
        this.basePackageList = Stream.of(basePackages).collect(Collectors.toList());
    }

    @Override
    public Pointcut getPointcut() {
        if(pointcut == null){
            this.pointcut = new StaticMethodMatcherPointcut() {
                @Override
                public boolean matches(Method method, Class<?> targetClass) {
                    if(method.getDeclaringClass() == Object.class || targetClass == null || targetClass.getPackage() == null){
                        return false;
                    }
                    String targetClassName = targetClass.getName();
                    String packageName = targetClass.getPackage().getName();
                    if(!basePackageList.stream().anyMatch(basePackage -> match(targetClassName, packageName, basePackage))){
                        return false;
                    }
                    if(targetClass.isAnnotationPresent(IgnoreLog.class) || method.isAnnotationPresent(IgnoreLog.class)){
                        return false;
                    }
                    return targetClass.isAnnotationPresent(Controller.class) || targetClass.isAnnotationPresent(RestController.class);
                }
            };
        }
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        if(advice == null){
            this.advice = new ControllerLogAdvice(paramTransfer, controllerLogCustomizerObjectProvider.getIfAvailable());
        }
        return advice;
    }

    private boolean match(String targetClassName, String packageName, String basePackage){
        return pathMatcher.isPattern(basePackage) ? pathMatcher.match(basePackage, packageName) : targetClassName.startsWith(basePackage);
    }
}
