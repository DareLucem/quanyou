package cn.edu.xaut.quanyou.DTO;

import cn.edu.xaut.quanyou.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TeamQuery extends PageRequest implements Serializable {
    private Long id;
    private List<Long> idList;
    private String name;
    private String description;
    private Integer maxNum;
    private Date expireTime;
    private Long userId;
    private Integer status;
    private String password;
    private String searchText;

}
