package com.tedrain.mybatis.po;

import lombok.Data;

import java.util.Date;

/**
 * Lombok核心特征是你需要用注解来创建代码,目的是减少你要写的样板代码的数量
 */
@Data
public class User {
    private Integer id;
    private String username;
    private Date birthday;
    private String sex;
    private String address;
}
