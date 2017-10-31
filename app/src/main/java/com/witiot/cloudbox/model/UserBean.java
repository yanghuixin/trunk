package com.witiot.cloudbox.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by lixin on 2017/8/14.
 */

//@Table(name = "UserBean")
public class UserBean {
//    @Column(name = "id", isId = true)
    private int id;
//    @Column(name = "customerId")
    String customerId;//手机号
//    @Column(name = "tok")
    String tok;
}
