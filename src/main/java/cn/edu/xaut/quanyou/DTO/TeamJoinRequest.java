package cn.edu.xaut.quanyou.DTO;

import lombok.Data;

import java.io.Serializable;
@Data
public class TeamJoinRequest implements Serializable {
    private Long teamId;
    private String password;
}
