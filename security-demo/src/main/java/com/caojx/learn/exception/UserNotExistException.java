package com.caojx.learn.exception;

/**
 * 自定义异常
 *
 * @author caojx
 * @version $Id: UserNotExistException.java,v 1.0 2020/2/17 10:35 下午 caojx
 * @date 2020/2/17 10:35 下午
 */
public class UserNotExistException extends RuntimeException {

    private String id;

    public UserNotExistException(String id) {
        super("user not exist");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}