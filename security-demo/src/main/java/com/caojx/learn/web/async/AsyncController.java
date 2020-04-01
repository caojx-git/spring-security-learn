package com.caojx.learn.web.async;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

/**
 * 类注释，描述
 *
 * @author caojx
 * @version $Id: AsyncController.java,v 1.0 2020/2/18 5:32 下午 caojx
 * @date 2020/2/18 5:32 下午
 */
@RestController
public class AsyncController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MockQueue mockQueue;

    @Autowired
    private DeferredResultHolder deferredResultHolder;

    /**
     * 同步的处理方式
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/order")
    public String order() throws Exception {
        logger.info("主线程开始");
        Thread.sleep(1000);
        logger.info("主线程返回");
        return "success";
    }

    /**
     * 异步的处理方式，注意测试的时候关闭拦截器，避免相关日志输出混乱
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/order2")
    public Callable<String> order2() throws Exception {
        logger.info("主线程开始");

        Callable<String> result = new Callable<String>() {
            @Override
            public String call() throws Exception {
                logger.info("副线程开始");
                Thread.sleep(1000);
                logger.info("副线程返回");
                return "success";
            }
        };

        logger.info("主线程返回");
        return result;
    }


    /**
     * 使用DeferredResult 异步处理
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/order3")
    public DeferredResult<String> order3() throws Exception {
        logger.info("主线程开始");

        // 模拟生成8位的订单号
        String orderNumber = RandomStringUtils.randomNumeric(8);
        // 模拟发送下单消息到消息队里
        mockQueue.setPlaceOrder(orderNumber);

        // 获取异步结果
        DeferredResult<String> result = new DeferredResult<>();
        deferredResultHolder.getMap().put(orderNumber, result);

        logger.info("主线程返回");
        return result;
    }
}