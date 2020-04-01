package com.caojx.learn.dto;

/**
 * 类注释，描述
 *
 * @author caojx
 * @version $Id: FileInfo.java,v 1.0 2020/2/18 4:42 下午 caojx
 * @date 2020/2/18 4:42 下午
 */
public class FileInfo {

    private String path;

    public FileInfo(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}