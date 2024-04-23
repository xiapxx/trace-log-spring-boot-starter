package io.github.xiapxx.starter.tracelog.core;

import io.github.xiapxx.starter.tracelog.annotation.EnableTraceLog;
import io.github.xiapxx.starter.tracelog.core.controllerlog.ControllerLogPointcutAdvisor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author xiapeng
 * @Date 2024-04-22 17:19
 */
public class TraceLogRegistrar implements ImportBeanDefinitionRegistrar {

    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableTraceLog.class.getName()));

        registerParamTransfer(registry);

        String[] basePackages = annoAttrs.getStringArray("basePackages");
        boolean controllerLog = annoAttrs.getBoolean("controllerLog");
        registerControllerLog(registry, basePackages, controllerLog);
    }

    /**
     * 注册参数转换器
     *
     * @param registry registry
     */
    private void registerParamTransfer(BeanDefinitionRegistry registry){
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ParamTransfer.class);
        registry.registerBeanDefinition(ParamTransfer.class.getName(), beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * 注册controller层日志
     *
     * @param registry registry
     * @param basePackages basePackages
     * @param controllerLog controllerLog
     */
    private void registerControllerLog(BeanDefinitionRegistry registry, String[] basePackages, boolean controllerLog){
        if(!controllerLog){
            return;
        }
        try {
            Class.forName("org.springframework.web.WebApplicationInitializer");
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ControllerLogPointcutAdvisor.class, () -> new ControllerLogPointcutAdvisor(basePackages));
            registry.registerBeanDefinition(ControllerLogPointcutAdvisor.class.getName(), beanDefinitionBuilder.getBeanDefinition());
        } catch (ClassNotFoundException e) {
        }
    }


}
