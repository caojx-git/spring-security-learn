package com.caojx.learn.web.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 模拟消息队列
 *
 * @author caojx
 * @version $Id: MockQueue.java,v 1.0 2020/2/18 6:09 下午 caojx
 * @date 2020/2/18 6:09 下午
 */
@Component
public class MockQueue {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 下单消息
     */
    private String placeOrder;

    /**
     * 下单完成消息
     */
    private String completeOrder;

    /**
     * set 方法模拟往消息队列中放消息
     *
     * @param placeOrder
     */
    public void setPlaceOrder(String placeOrder) {
        new Thread(() -> {
            logger.info("接到下单请求," + placeOrder);

            // 进行处理。。。
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 完成处理
            this.completeOrder = placeOrder;
            logger.info("下单请求处理完毕，" + placeOrder);
        }).start();

    }

    public String getPlaceOrder() {
        return placeOrder;
    }

    public String getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(String completeOrder) {
        this.completeOrder = completeOrder;
    }
}