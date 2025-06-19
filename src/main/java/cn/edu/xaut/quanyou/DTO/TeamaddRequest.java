package cn.edu.xaut.quanyou.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class TeamaddRequest implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;
    private String name;
    private String description;
    private Integer maxNum;
    private Date expireTime;
    private Long userId;
    private Integer status;
    private String password;
}
