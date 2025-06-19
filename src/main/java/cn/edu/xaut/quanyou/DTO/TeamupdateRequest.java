package cn.edu.xaut.quanyou.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class TeamupdateRequest implements Serializable {
    private static final long serialVersionUID = 11231234154L;
    private Long id;
    private String name;
    private String description;
    private Date expireTime;
    private Integer status;
    private String password;

}
