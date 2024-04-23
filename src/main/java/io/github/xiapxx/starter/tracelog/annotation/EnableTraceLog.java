package io.github.xiapxx.starter.tracelog.annotation;

import io.github.xiapxx.starter.tracelog.core.TraceLogRegistrar;
import org.springframework.context.annotation.Import;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启日志跟踪
 *
 * @Author xiapeng
 * @Date 2024-04-22 16:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(TraceLogRegistrar.class)
public @interface EnableTraceLog {

    /**
     * 记录controller层日志的范围;
     * controllerLog=true时必填;
     *
     * @return
     */
    String[] basePackages() default {};

    /**
     * 是否记录controller层日志
     *
     * @return true/false
     */
    boolean controllerLog() default true;

    /**
     * 是否记录feign层的日志
     *
     * @return true/false
     */
    boolean feignLog() default true;

    /**
     * 是否记录restTemplate的日志
     *
     * @return true/false
     */
    boolean restTemplateLog() default true;

}
