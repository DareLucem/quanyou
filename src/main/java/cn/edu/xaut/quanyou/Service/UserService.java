package cn.edu.xaut.quanyou.Service;

import cn.edu.xaut.quanyou.Model.User;
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

    int userLogout(HttpServletRequest request);

  List<User> searchUsersByTags(List<String> tagNameList);
}
