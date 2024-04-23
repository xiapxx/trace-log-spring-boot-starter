package io.github.xiapxx.starter.tracelog.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ClassUtils;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.StringJoiner;

/**
 * 参数转换
 *
 * @Author xiapeng
 * @Date 2024-04-23 10:27
 */
public class ParamTransfer {

    private static final String EMPTY = "";

    private ObjectMapper objectMapper;

    public ParamTransfer(){
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * 入参转换为字符串
     *
     * @param parameters parameters
     * @param args args
     * @return 入参字符串
     */
    public String inputParamToString(Parameter[] parameters, Object[] args) {
        if(args == null || args.length == 0){
            return EMPTY;
        }
        if(args.length == 1){
            return  "\t" + toParamString(args[0]);
        }
        StringJoiner paramJoiner = new StringJoiner("\n");
        for (int i = 0; i < args.length; i++) {
            String paramName = parameters[0].getName();
            String paramValue = toParamString(args[i]);
            paramJoiner.add("\t" + paramName + " = " + paramValue);
        }
        return paramJoiner.toString();
    }

    /**
     * 出参转换为字符串
     *
     * @param result result
     * @return 出参字符串
     */
    public String outputParamToString(Object result){
        return toParamString(result);
    }

    /**
     * 转换为出参字符串
     *
     * @param arg arg
     * @return 出参字符串
     */
    private String toParamString(Object arg){
        if(arg == null){
            return EMPTY;
        }
        try {
            if(arg instanceof String){
                return (String) arg;
            }
            if(ClassUtils.isPrimitiveOrWrapper(arg.getClass())){
                return arg.toString();
            }
            return objectMapper.writeValueAsString(arg);
        } catch (Throwable e) {
            return EMPTY;
        }
    }

}
