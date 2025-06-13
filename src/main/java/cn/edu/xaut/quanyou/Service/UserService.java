package cn.edu.xaut.quanyou.Service;

import cn.edu.xaut.quanyou.Model.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
* @author Dare
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-05-16 19:05:13
*/
public interface UserService extends IService<User> {

    /**
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 密码校验
     * @return
     */
  long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode);

    /**
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return 用户信息
     */
  User userLogin(String userAccount, String userPassword,  HttpServletRequest request);

    /**
     *
     * @param username
     * @return 搜索用户
     */
  List<User> searchuser(String username);

    /**
     *
     * @param OriginUser
     * @return 安全用户
     */
    User getSafetyUser(User OriginUser);

  /**
   * 登出账号
   * @param request
   * @return
   */
    int userLogout(HttpServletRequest request);

  /**
   * 通过标签搜索用户
   * @param tagNameList
   * @return
   */
  Page<User> searchUsersByTags(List<String> tagNameList,int pageNum,int pageSize);

  /**
   * 修改个人用户 信息
   * @param user
   * @param loginuser
   * @return
   */
  int updateUser(User user, User loginuser);
/**
   * 是否为管理员
   * @param request
   * @return
   */
  boolean isAdmin(HttpServletRequest request);
  /**
   * 获取当前登录用户
   * @param request
   * @return
   */
  User getloginuser(HttpServletRequest request);

  /**
   * 用户信息检测
   * @param userAccount
   * @param userPassword
   */
  void  correctAccountAndPassword(String userAccount, String userPassword);
 long updatePassword(long id, String oldPassword, String newPassword);


   IPage<User> recommendUsers(Long  PageSize, Long PageNum,User user);

}
