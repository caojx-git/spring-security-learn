package com.caojx.learn.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户查询请求参数
 *
 * @author caojx
 * @version $Id: UserQueryCondition.java,v 1.0 2020/2/17 5:19 下午 caojx
 * @date 2020/2/17 5:19 下午
 */
@Data
public class UserQueryCondition {

    private String username;

    @ApiModelProperty(value = "用户年龄起始值")
    private Integer age;

    @ApiModelProperty(value = "用户年龄终止值")
    private Integer ageTo;
}