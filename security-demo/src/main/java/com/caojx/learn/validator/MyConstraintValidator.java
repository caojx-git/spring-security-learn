package com.caojx.learn.validator;

import com.caojx.learn.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义校验器
 * <p>
 * 两个泛型
 *  第一个泛型是说明这是那个注解的校验器
 *  第二个泛型是说明对那种类型的参数进行校验
 *
 * 注意：该校验器不需要添加任何注解，就可以被Spring自动识别成bean，只要实现ConstraintValidator接口即可，校验器里边可以注入任何Spring容器中的bean
 *
 * @author caojx
 * @version $Id: MyConstraintValidator.java,v 1.0 2020/2/17 9:32 下午 caojx
 * @date 2020/2/17 9:32 下午
 */
public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object> {

    /**
     * 校验器里边可以注入任何Spring容器中的bean
     */
    @Autowired
    private HelloService helloService;

    /**
     * 初始化方法
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(MyConstraint constraintAnnotation) {
        System.out.println("my validator init");
    }

    /**
     * 参数校验方法，返回true 表示校验通过，false表示校验失败
     *
     * @param value
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        helloService.greeting("tom");
        System.out.println(value);
        return false;
    }

}