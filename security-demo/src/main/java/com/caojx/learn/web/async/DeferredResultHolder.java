package com.caojx.learn.web.async;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义 DeferredResultHolder 封装处理结果
 *
 * @author caojx
 * @version $Id: DeferredResultHolder.java,v 1.0 2020/2/18 6:13 下午 caojx
 * @date 2020/2/18 6:13 下午
 */
@Component
public class DeferredResultHolder {

    /**
     * key 订单号
     * value 处理结果
     */
    private Map<String, DeferredResult<String>> map = new HashMap<>();


    public Map<String, DeferredResult<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, DeferredResult<String>> map) {
        this.map = map;
    }
}