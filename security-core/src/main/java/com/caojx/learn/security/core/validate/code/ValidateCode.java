package com.caojx.learn.security.core.validate.code;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 验证码类
 *
 * @author caojx
 * @version $Id: ValidateCode.java,v 1.0 2020/2/21 9:18 下午 caojx
 * @date 2020/2/21 9:18 下午
 */
@Data
public class ValidateCode implements Serializable {

    /**
     * 随机数，验证码
     */
    private String code;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 构造函数，指定多少秒之后过期
     *
     * @param code
     * @param expireIn
     */
    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    /**
     * 构造函数指定验证码什么过期
     *
     * @param code
     * @param expireTime
     */
    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}