package com.caojx.learn.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 自定义SpringSocialConfigurer
 *
 * @author caojx
 * @version $Id: ImoocSpringSocialConfigurer.java,v 1.0 2020/3/3 7:03 下午 caojx
 * @date 2020/3/3 7:03 下午
 */
public class ImoocSpringSocialConfigurer extends SpringSocialConfigurer {

    /**
     *  社交登录，SocialAuthenticationFilter 过滤器拦截处理的url
     */
    private String filterProcessesUrl;

    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    public ImoocSpringSocialConfigurer(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }

    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        // 替换SocialAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URL= "/auth" 的地址为我们配置的地址
        filter.setFilterProcessesUrl(filterProcessesUrl);

        // 如果配置社交登录过滤器后处理器，这执行响应的逻辑
        if(socialAuthenticationFilterPostProcessor != null){
            socialAuthenticationFilterPostProcessor.process(filter);
        }
        return (T) filter;
    }

    public void setSocialAuthenticationFilterPostProcessor(SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor) {
        this.socialAuthenticationFilterPostProcessor = socialAuthenticationFilterPostProcessor;
    }
}