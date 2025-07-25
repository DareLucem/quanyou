package cn.edu.xaut.quanyou.DTO;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登陆请求体
 */
@Data
public class UserLoginRequest implements Serializable {
    private  static final long serialVersionUID = 1L;
    private String userAccount;
    private String userPassword;
}
