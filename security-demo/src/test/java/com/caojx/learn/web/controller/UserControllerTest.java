package com.caojx.learn.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 使用Restful API 对接口进行测试
 *
 * @author caojx
 * @version $Id: UserControllerTest.java,v 1.0 2020/2/17 4:57 下午 caojx
 * @date 2020/2/17 4:57 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * 引入mock mvc 环境，不会真的去启动tomcat
     */
    private MockMvc mockMvc;

    /**
     * 每次启动前构造一个伪造的 mvc 环境
     */
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * 查询用户列表测试用例
     *
     * @throws Exception
     */
    @Test
    public void whenQuerySuccess() throws Exception {
        // mockMvc.perform 执行 GET /user 请求
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                .param("username", "tom")
                .param("age", "18")
                .param("ageTo", "60")
                .param("size", "15") //分页设置，每页15条，查询第三页的数据，处于新年结果按照年龄降序排序
                .param("page", "3")
                .param("sort", "age,desc")
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // 数据格式为 json utf-8编码
                .andExpect(MockMvcResultMatchers.status().isOk()) // 期望的响应状态码 是 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3)) // 期望的响应数组长度是3，使用jsonPath表达式，使用文档见https://github.com/json-path/JsonPath
                .andReturn().getResponse().getContentAsString(); // 获取响应信息

        System.out.println(result);
    }

    /**
     * 获取用户详情测试用户
     *
     * @throws Exception
     */
    @Test
    public void whenGetInfoSuccess() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("tom"))
                .andReturn().getResponse().getContentAsString();

        System.out.println(result);
    }

    /**
     * 获取用户详情测试用户，看id是否只能接收数字
     *
     * @throws Exception
     */
    @Test
    public void whenGetInfoFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/a")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    /**
     * 测试用户创建
     *
     * @throws Exception
     */
    @Test
    public void whenCreateSuccess() throws Exception {

        // 建议对于时间类型的传递，不管是前端传递给后端，还是后端返回给前端，建议使用时间戳，由前端决定以什么样的格式显示

        // 前端传递给后端 时间戳
        Date date = new Date();
        System.out.println(date.getTime());

        String content = "{\"username\":\"tom\",\"pasword\":null,\"birthday\":" + date.getTime() + "}";

        String result = mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andReturn().getResponse().getContentAsString();

        // 打印user result 结果显示，后端响应给前端也是时间戳
        System.out.println(result);
    }

    /**
     * 测试用户修改
     *
     * @throws Exception
     */
    @Test
    public void whenUpdateSuccess() throws Exception {

        // 1年以后的时间，测试 @Past主机
        Date date = new Date(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        System.out.println(date.getTime());

        String content = "{\"id\":\"1\",\"username\":\"tom\",\"pasword\":null,\"birthday\":" + date.getTime() + "}";

        String result = mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andReturn().getResponse().getContentAsString();

        System.out.println(result);
    }

    /**
     * 删除用户测试
     *
     * @throws Exception
     */
    @Test
    public void whenDeleteSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}