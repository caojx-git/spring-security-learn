package com.caojx.learn.code;

import com.caojx.learn.security.core.validate.code.image.ImageCode;
import com.caojx.learn.security.core.validate.code.ValidateCodeGenerator;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 覆盖默认的图形验证码生成器
 *
 * @author caojx
 * @version $Id: DemoImageValidateCodeGenerator.java,v 1.0 2020/2/22 6:47 下午 caojx
 * @date 2020/2/22 6:47 下午
 */
//@Component("imageValidateCodeGenerator")
public class DemoImageValidateCodeGenerator implements ValidateCodeGenerator {

    @Override
    public ImageCode generate(ServletWebRequest servletWebRequest) {
        System.out.println("更高级的图形验证码生成器"); // 这里没有写过多的实现，只是为了验证会覆盖默认的图形验证码生成逻辑
        return null;
    }
}