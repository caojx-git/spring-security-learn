package com.caojx.learn.security.core.validate.code;

import com.caojx.learn.security.core.properties.SecurityConstants;

/**
 * 验证码类型枚举
 *
 * @author caojx
 * @version $Id: ValidateCodeType.java,v 1.0 2020/3/1 2:19 下午 caojx
 * @date 2020/3/1 2:19 下午
 */
public enum ValidateCodeType {

    /**
     * 短信验证码
     */
    SMS {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_SMS;
        }
    },

    /**
     * 图片验证码
     */
    IMAGE {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
        }
    };

    /**
     * 校验时从请求中获取的参数的名字
     *
     * @return
     */
    public abstract String getParamNameOnValidate();
}