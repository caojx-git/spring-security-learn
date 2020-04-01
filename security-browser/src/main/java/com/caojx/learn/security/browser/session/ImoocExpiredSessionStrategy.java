package com.caojx.learn.security.browser.session;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 如果你针对原来的请求去做一些记录，比如说到底是谁上来把前一个用户踢下来，在服务器端想做一个记录的话，你可以去实现这样一个接口叫SessionInformationExpiredStrategy
 *
 * @author caojx
 * @version $Id: ImoocExpiredSessionStrategy.java,v 1.0 2020/3/8 5:29 下午 caojx
 * @date 2020/3/8 5:29 下午
 */
public class ImoocExpiredSessionStrategy extends AbstractSessionStrategy  implements SessionInformationExpiredStrategy {

    public ImoocExpiredSessionStrategy(String invalidSessionUrl) {
        super(invalidSessionUrl);
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
//        sessionInformationExpiredEvent.getResponse().setContentType("application/json;charset=UTF-8");
//        sessionInformationExpiredEvent.getResponse().getWriter().write("并发登录！");

        // 同时支持JSON与HTML格式数据支持
        onSessionInvalid(sessionInformationExpiredEvent.getRequest(), sessionInformationExpiredEvent.getResponse());

    }

    @Override
    protected boolean isConcurrency() {
        return true;
    }
}