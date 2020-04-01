package com.caojx.learn.web.controller;

import com.caojx.learn.dto.FileInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * 类注释，描述
 *
 * @author caojx
 * @version $Id: FileController.java,v 1.0 2020/2/18 4:41 下午 caojx
 * @date 2020/2/18 4:41 下午
 */
@RestController
@RequestMapping("/file")
public class FileController {

    // 这里为了演示假设是写到本地，如果要写到其他服务器，可以读取文件的输入流，然后写到其他目标服务器
    private String folder = "/Users/caojx/Desktop/spring-security-learn/security-demo/src/main/java/com/caojx/learn/web/controller";

    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping
    public FileInfo upload(MultipartFile file) throws Exception {
        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());

        File localFile = new File(folder, new Date().getTime() + ".txt");
        file.transferTo(localFile);
        return new FileInfo(localFile.getAbsolutePath());
    }

    /**
     * 文件下载
     *
     * @param id
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/{id}")
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try (InputStream inputStream = new FileInputStream(new File(folder, id + ".txt"));
             OutputStream outputStream = response.getOutputStream();) {

            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=test.txt");

            // 使用commons-io依赖中提供的IO工具类，文件的输入流中的内容，写入到文件的输出流中
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }
    }
}