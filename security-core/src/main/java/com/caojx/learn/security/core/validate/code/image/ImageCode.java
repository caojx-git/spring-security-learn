package com.caojx.learn.security.core.validate.code.image;


import com.caojx.learn.security.core.validate.code.ValidateCode;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 图形验证码类
 *
 * @author caojx
 * @version $Id: ImageCode.java,v 1.0 2020/2/21 9:18 下午 caojx
 * @date 2020/2/21 9:18 下午
 */
@Data
public class ImageCode extends ValidateCode {

    /**
     * 图片
     */
    private BufferedImage image;

    /**
     * 构造函数，指定多少秒之后过期
     *
     * @param image
     * @param code
     * @param expireIn
     */
    public ImageCode(BufferedImage image, String code, int expireIn){
        super(code, expireIn);
        this.image = image;
    }

    /**
     * 构造函数指定验证码什么过期
     *
     * @param image
     * @param code
     * @param expireTime
     */
    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime){
        super(code, expireTime);
        this.image = image;
    }
}