package io.github.xiapxx.starter.tracelog.core.resttemplatelog;

import io.github.xiapxx.starter.tracelog.core.controllerlog.ControllerLogAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.StringJoiner;

/**
 * @Author xiapeng
 * @Date 2024-04-23 14:06
 */
public class RestTemplateLogRegistrar implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(ControllerLogAdvice.class);

    @Autowired
    private ObjectProvider<List<RestTemplate>> restTemplateObjectProvider;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof RestTemplate){
            RestTemplate restTemplate = (RestTemplate) bean;
            restTemplate.getInterceptors().add(requestLogInterceptor());
        }
        return bean;
    }

    /**
     * 请求日志拦截器
     *
     * @return 日志拦截器
     */
    private ClientHttpRequestInterceptor requestLogInterceptor(){
        return (request, body, execution) -> {
            long start = System.currentTimeMillis();
            Throwable error = null;
            BufferingClientHttpResponse bufferHttpResponse = null;
            try {
                ClientHttpResponse response = execution.execute(request, body);
                bufferHttpResponse = new BufferingClientHttpResponse(response);
            } catch (Throwable e) {
                error = e;
                throw e;
            } finally {
                printLog(request, body, bufferHttpResponse, start, error);
            }
            return bufferHttpResponse;
        };
    }

    /**
     * 打印日志
     *
     * @param request request
     * @param requestBody requestBody
     * @param bufferHttpResponse bufferHttpResponse
     * @param start start
     * @param error error
     */
    private void printLog(HttpRequest request, byte[] requestBody, BufferingClientHttpResponse bufferHttpResponse, long start, Throwable error){
        try {
            StringJoiner logJoiner = new StringJoiner("\n");
            logJoiner.add("");
            logJoiner.add("=====================================restTemplate===================================");
            logJoiner.add(request.getMethod().name() + " " + request.getURI());
            logJoiner.add("request : ");
            logJoiner.add("\t" + new String(requestBody));
            logJoiner.add("response : ");
            if(error == null){
                logJoiner.add("\t" + bufferHttpResponse.getBodyString());
            }
            logJoiner.add("Usage time : " + (System.currentTimeMillis() - start) + "ms");
            logJoiner.add("=====================================restTemplate===================================");
            logJoiner.add("");
            if(error == null){
                log.info(logJoiner.toString());
            } else {
                log.error(logJoiner.toString(), error);
            }
        } catch (Throwable e) {
        }
    }
}
