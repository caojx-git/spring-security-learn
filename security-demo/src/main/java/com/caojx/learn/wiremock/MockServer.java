package com.caojx.learn.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * 连接WireMock服务，设置请求什么url响应什么数据
 *
 * WireMock安装见：
 * http://wiremock.org/docs/running-standalone/
 * https://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-standalone/2.26.0/wiremock-standalone-2.26.0.jar
 *
 * @author caojx
 * @version $Id: MockServer.java,v 1.0 2020/2/18 8:15 下午 caojx
 * @date 2020/2/18 8:15 下午
 */
public class MockServer {

    public static void main(String[] args) throws IOException {
        // 连接 WireMock服务，指定WireMock服务与端口
        WireMock.configureFor("127.0.0.1", 8082);
        // 每次执行都把之前的所有配置都清空
        WireMock.removeAllMappings();

        // 伪造请求对应的响应的数据
        String body = "{\"id\":1}";
        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/order/1"))
                .willReturn(WireMock.aResponse()
                        .withBody(body)
                        .withStatus(200)));


        //从文件中读取响应数据
        mock("/order/2", "02.json");
        mock("/order/3", "03.json");

    }

    /**
     * 从文件中读取响应数据
     *
     * @param url
     * @param fileName
     * @throws IOException
     */
    public static void mock(String url, String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("mock/response/" + fileName);
        String content = FileUtils.readFileToString(resource.getFile(), "UTF-8");

        WireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo(url))
                .willReturn(WireMock.aResponse()
                        .withBody(content)
                        .withStatus(200)));

    }
}