package com.caojx.learn.dto;

import com.caojx.learn.validator.MyConstraint;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * 用户类实体
 *
 * @author caojx
 * @version $Id: User.java,v 1.0 2020/2/17 5:12 下午 caojx
 * @date 2020/2/17 5:12 下午
 */
public class User {

    /**
     * @JsonView 使用，
     * 1.使用接口来声明多个视图
     */
    public interface UserSimpleView {}

    public interface UserDetailView extends UserSimpleView {}

    private String id;

    @MyConstraint(message = "这是一个测试")
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Past(message = "生日必须是过去的时间")
    private Date birthday;

    @JsonView(UserSimpleView.class)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return
     * @JsonView 使用，
     * 2.在值对象的get方法上指定视图
     */
    @JsonView(UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return
     * @JsonView 使用，
     * 2.在值对象的get方法上指定视图
     */
    @JsonView(UserDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonView(UserSimpleView.class)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}