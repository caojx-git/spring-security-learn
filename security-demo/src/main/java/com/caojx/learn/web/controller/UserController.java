package com.caojx.learn.web.controller;

import com.caojx.learn.dto.User;
import com.caojx.learn.dto.UserQueryCondition;
import com.caojx.learn.exception.UserNotExistException;
import com.caojx.learn.security.core.properties.SecurityProperties;
import com.fasterxml.jackson.annotation.JsonView;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户Controller，该类配合UserControllerTest测试类使用
 *
 * @author caojx
 * @version $Id: UserController.java,v 1.0 2020/2/17 5:08 下午 caojx
 * @date 2020/2/17 5:08 下午
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

//    @Autowired
//    private AppSingUpUtils appSingUpUtils;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 注册用户
     *
     * @param user
     */
    @PostMapping("/regist")
    public void regist(User user, HttpServletRequest request) {

        // 假设注册成功了用户，DB操作。。。

        //用户完成注册过程以后不管是注册和绑定，他都会拿到一个用户的唯一标识(业务系统的用户的userId)
        String userId = user.getUsername();

        // 将业务系统的userId与社交用户信息保存到UserConnection表
        providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));

        // APP架构注册用户，userId与社交用户信息保存到UserConnection表
//        appSingUpUtils.doPostSignUp(userId, new ServletWebRequest(request));
    }

    /**
     * 获取获取认证的用户信息 1
     *
     * @return
     */
    @GetMapping("/me1")
    public Object getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取认证的用户信息 2
     *
     * @param authentication
     * @return
     */
    @GetMapping("/me2")
    public Object getCurrentUser(Authentication authentication) {
        return authentication;
    }

    /**
     * 获取认证的用户信息中的UserDetails信息 3
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/me3")
    public Object getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }

    /**
     * 获取jwt中添加额外信息
     *
     * @param authentication
     * @param request
     * @throws IllegalArgumentException
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/me4")
    public Object getCurrentUser(Authentication authentication, HttpServletRequest request) throws Exception {

        // 获取JWT，后去header Authorization中bearer后边的部分
        String token = StringUtils.substringAfter(request.getHeader("Authorization"), "bearer ");

        // 使用秘钥解析jwt token
        Claims claims = Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSigningKey().getBytes("UTF-8"))
                .parseClaimsJws(token).getBody();

        // 获取为JWT扩展的信息
        String company = (String) claims.get("company");

        System.out.println(company);

        // 获取 用户认证信息，可以直接在参数中声明Authentication user直接获取，Spring会自动注入这个对象
        // return SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }


    /**
     * 获取用户列表
     * <p>
     * JsonView 使用
     * 3.在Controller方法上指定视图
     *
     * @param userQueryCondition
     * @param pageable
     * @return
     */
    @GetMapping
    @JsonView(User.UserSimpleView.class)
    @ApiOperation(value = "用户查询服务")
    public List<User> query(UserQueryCondition userQueryCondition, @PageableDefault(page = 1, size = 15, sort = "age,desc") Pageable pageable) {

        System.out.println(ReflectionToStringBuilder.toString(userQueryCondition));

        System.out.println(pageable.getPageSize());
        System.out.println(pageable.getPageNumber());
        System.out.println(pageable.getSort());

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        return users;
    }

    /**
     * 使用正则表达式，限制id只能接受数字
     * <p>
     * JsonView 使用
     * 3.在Controller方法上指定视图
     *
     * @param id
     * @return
     */
    @JsonView(User.UserDetailView.class)
    @GetMapping(value = "/{id:\\d+}")
    public User getInfo(@PathVariable(value = "id") String id) {

//        throw new UserNotExistException(id);

        System.out.println("进入调用getInfo服务");
        User user = new User();
        user.setUsername("tom");
        return user;
    }

    /**
     * Post方法使用@RequestBody注解映射请求体到Java方法的参数，User.java类新增id字段
     * <p>
     * BindingResult需要配合@Valid注解使用，单参数校验不通过的时候，错误信息就会放到BindingResult，进入到方法体中，而不会直接打回
     *
     * @param user
     * @return
     */
    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult errors) {

        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach(error -> System.out.println(error.getDefaultMessage()));
        }

        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());


        System.out.println(user.getBirthday());

        user.setId("1");
        return user;
    }

    @PutMapping("/{id:\\d+}")
    public User update(@Valid @Validated  @RequestBody User user, BindingResult errors) {

        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach(error -> {
                FieldError fieldError = (FieldError) error;
                String message = fieldError.getField() + " " + error.getDefaultMessage();
                System.out.println(message);

                System.out.println(error.getDefaultMessage());
            });
        }

        System.out.println(user.getId());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getBirthday());

        user.setId("1");
        return user;
    }

    /**
     * 删除用户
     *
     * @param id
     */
    @DeleteMapping("/{id:\\d+}")
    public void update(@ApiParam("用户id") @PathVariable String id) {
        System.out.println(id);
    }
}