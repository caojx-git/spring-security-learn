package com.caojx.learn.security.core.social;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 绑定成功与解绑成功的视图
 *
 * @author caojx
 * @version $Id: ImoocConnectView.java,v 1.0 2020/3/7 9:53 下午 caojx
 * @date 2020/3/7 9:53 下午
 */
public class ImoocConnectView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setContentType("text/html;charset=UTF-8");
        if (model.get("connection") == null) {
            httpServletResponse.getWriter().write("<h3>解绑成功</h3>");
        } else {
            httpServletResponse.getWriter().write("<h3>绑定成功</h3>");
        }
    }
}