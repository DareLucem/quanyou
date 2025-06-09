package cn.edu.xaut.quanyou.DTO;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserUpdatepswRequest implements Serializable {
    private int id;
    private String oldPassword;
    private String newPassword;
}
