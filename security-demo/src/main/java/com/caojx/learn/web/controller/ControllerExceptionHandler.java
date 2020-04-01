package com.caojx.learn.web.controller;

import com.caojx.learn.exception.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类
 *
 * @author caojx
 * @version $Id: ControllerExceptionHandler.java,v 1.0 2020/2/17 10:38 下午 caojx
 * @date 2020/2/17 10:38 下午
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * 对于所有抛出在Controller中UserNotExistException的地方都会到这里来处理，返回响应异常的格式按照自定的格式返回
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(UserNotExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleUsrNotExistException(UserNotExistException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", ex.getId());
        result.put("message", ex.getMessage());
        return result;
    }
}