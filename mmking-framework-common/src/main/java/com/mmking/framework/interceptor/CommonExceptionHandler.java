package com.mmking.framework.interceptor;

import com.google.common.collect.ImmutableMap;
import com.mmking.framework.exception.MMkingException;
import com.mmking.framework.model.response.CommonCode;
import com.mmking.framework.model.response.ResponseResult;
import com.mmking.framework.model.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;


@Slf4j
@ControllerAdvice   //注解拦截所有controller  注意要让启动器扫描到
public class CommonExceptionHandler {


    private static ImmutableMap<Class<? extends Throwable>, ResultCode> handleMap;

    /*不可见异常构建器*/
    private static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder= ImmutableMap.builder();

    static {
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }


    /*可预见异常*/
    @ResponseBody
    @ExceptionHandler(MMkingException.class)
    public ResponseResult handleException(MMkingException e){

        /*取出回执信息*/
        ResultCode resultCode =  e.getResultCode();

        return  new ResponseResult(resultCode);
    }


    /*不可预见异常*/
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public  ResponseResult handleAllException(Exception e){
        if (handleMap==null){
            handleMap = builder.build();
        }

        ResultCode resultCode = handleMap.get(e.getClass());
        /*说明在map内*/
        if (resultCode!=null){
            return  new ResponseResult(resultCode);
        }

        return  new ResponseResult(CommonCode.SERVER_ERROR);

    }
}