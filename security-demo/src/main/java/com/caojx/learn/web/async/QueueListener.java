package com.caojx.learn.web.async;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 监听器，监听模拟的消息队列中CompleteOrder的值
 *
 * @author caojx
 * @version $Id: QueueListener.java,v 1.0 2020/2/18 6:32 下午 caojx
 * @date 2020/2/18 6:32 下午
 */
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MockQueue mockQueue;

    @Autowired
    private DeferredResultHolder deferredResultHolder;

    /**
     * ContextRefreshedEvent这个事件是spring初始化完毕的一个事件
     *
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(() -> {
            //将以下while循环放到一个单开的 thread 线程  防止主线程死循环
            //监听 mockQueue 中的 completeOrder
            while (true) {
                if (StringUtils.isNotBlank(mockQueue.getCompleteOrder())) {
                    String orderNumber = mockQueue.getCompleteOrder();
                    logger.info("返回订单处理结果:" + orderNumber);

                    //异步处理完成
                    deferredResultHolder.getMap().get(orderNumber).setResult("place order success");
                    //表明任务已经处理完了
                    mockQueue.setCompleteOrder(null);
                } else {
                    try {
                        //completeOrder 中没有值，则睡眠100毫秒
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}