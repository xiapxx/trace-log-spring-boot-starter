package io.github.xiapxx.starter.tracelog.core.controllerlog;

import io.github.xiapxx.starter.tracelog.core.ParamTransfer;
import io.github.xiapxx.starter.tracelog.interfaces.ControllerLogCustomizer;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.StringJoiner;

/**
 * @Author xiapeng
 * @Date 2024-04-22 17:35
 */
public class ControllerLogAdvice implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ControllerLogAdvice.class);

    private ParamTransfer paramTransfer;

    private ControllerLogCustomizer controllerLogCustomizer;

    public ControllerLogAdvice(ParamTransfer paramTransfer, ControllerLogCustomizer controllerLogCustomizer){
        this.paramTransfer = paramTransfer;
        this.controllerLogCustomizer = controllerLogCustomizer;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        ControllerLogContext controllerLogContext = newControllerLogContext(invocation);
        Object result = null;
        try {
            result = invocation.proceed();
        } catch (Throwable e) {
            controllerLogContext.setError(e);
            throw e;
        } finally {
            controllerLogContext.setResult(result);
            printLog(controllerLogContext);
        }
        return result;
    }

    private ControllerLogContext newControllerLogContext(MethodInvocation invocation){
        ControllerLogContext controllerLogContext = new ControllerLogContext();
        controllerLogContext.setStartTimeMills(System.currentTimeMillis());
        controllerLogContext.setHttpServletRequest(getHttpRequest());
        controllerLogContext.setMethod(invocation.getMethod());
        controllerLogContext.setArgs(invocation.getArguments());
        return controllerLogContext;
    }

    private HttpServletRequest getHttpRequest(){
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 打印日志
     *
     * @param controllerLogContext controllerLogContext
     */
    private void printLog(ControllerLogContext controllerLogContext){
        try {
            if(controllerLogCustomizer != null && !controllerLogCustomizer.printLog(controllerLogContext)){
                return;
            }
            StringJoiner logJoiner = new StringJoiner("\n");
            logJoiner.add("");
            logJoiner.add("======================================================================================");
            logJoiner.add(controllerLogContext.getHttpServletRequest().getMethod() + " " + controllerLogContext.getHttpServletRequest().getRequestURL().toString());
            logJoiner.add("request : ");
            logJoiner.add(paramTransfer.inputParamToString(controllerLogContext.getMethod().getParameters(), controllerLogContext.getArgs()));
            logJoiner.add("response : ");
            if(controllerLogContext.getError() == null){
                logJoiner.add(paramTransfer.outputParamToString(controllerLogContext.getResult()));
            }
            logJoiner.add("Usage time : " + (System.currentTimeMillis() - controllerLogContext.getStartTimeMills()) + "ms");
            logJoiner.add("======================================================================================");
            logJoiner.add("");
            if(controllerLogContext.getError() == null){
                log.info(logJoiner.toString());
            } else {
                log.error(logJoiner.toString(), controllerLogContext.getError());
            }
        } catch (Throwable e) {
        }
    }

}
