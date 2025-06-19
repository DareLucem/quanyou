package cn.edu.xaut.quanyou.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TeamUserVo implements Serializable {
    private static final long serialVersionUID = 1899063007109226944L;
    /**
     * id
     */

    private Long id;

    /**
     * 队伍名称
     */

    private String name;

    /**
     * 描述
     */

    private String description;

    /**
     * 最大人数
     */

    private Integer maxNum;

    /**
     * 过期时间
     */

    private Date expireTime;

    /**
     * 用户id（队长 id）
     */

    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */

    private Integer status;



    /**
     * 创建时间
     */

    private Date createTime;

    /**
     *
     */

    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 创建人
     */
     private  UserVO CreateUser;
     /**
     * 已加入的队伍人数
     */
      private Integer hasJoinNum;
    /**
     *  已加入用户的id
     */
    private List<Long> hasJoinUsersid;

}
