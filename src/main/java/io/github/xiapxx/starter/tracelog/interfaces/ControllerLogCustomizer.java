package io.github.xiapxx.starter.tracelog.interfaces;

import io.github.xiapxx.starter.tracelog.core.controllerlog.ControllerLogContext;

/**
 * @Author xiapeng
 * @Date 2024-04-23 11:31
 */
public interface ControllerLogCustomizer {

    /**
     * 自定义处理日志
     *
     * @param controllerLogContext controllerLogContext
     * @return 是否打印默认的日志; true/false
     */
    boolean printLog(ControllerLogContext controllerLogContext);

}
