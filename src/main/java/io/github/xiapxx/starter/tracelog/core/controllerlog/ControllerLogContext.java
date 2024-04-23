package io.github.xiapxx.starter.tracelog.core.controllerlog;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Author xiapeng
 * @Date 2024-04-22 18:01
 */
public class ControllerLogContext {

    private long startTimeMills;

    private HttpServletRequest httpServletRequest;

    private Method method;

    private Object[] args;

    private Object result;

    private Throwable error;

    public long getStartTimeMills() {
        return startTimeMills;
    }

    public void setStartTimeMills(long startTimeMills) {
        this.startTimeMills = startTimeMills;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
