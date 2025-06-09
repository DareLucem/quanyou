package cn.edu.xaut.quanyou.Service.impl;

import cn.edu.xaut.quanyou.Exception.BuessisException;
import cn.edu.xaut.quanyou.Mapper.UserMapper;
import cn.edu.xaut.quanyou.Model.User;
import cn.edu.xaut.quanyou.Service.UserService;
import cn.edu.xaut.quanyou.common.ErrorCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cn.edu.xaut.quanyou.Contant.UserContant.ADMIN_ROLE;
import static cn.edu.xaut.quanyou.Contant.UserContant.USER_LOGIN_STATE;


/**
* @author 86147
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-05-16 19:05:13
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
   @Resource
    private UserMapper  userMapper;


    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 密码校验
     * @return 用户id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode) {
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            throw new BuessisException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        this.correctAccountAndPassword(userAccount, userPassword);
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BuessisException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        }

        QueryWrapper<User> Wrapper = new QueryWrapper<User>();
        Wrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(Wrapper);
        if(count>0){
            throw new BuessisException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        QueryWrapper<User> pWrapper = new QueryWrapper<User>();
        pWrapper.eq("planetCode",planetCode);
        long countp = userMapper.selectCount(pWrapper);
        if(countp>0){
            throw new BuessisException(ErrorCode.PARAMS_ERROR, "编号重复");
        }
//        密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String bcryptPassword= encoder.encode(userPassword);
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(bcryptPassword);
        user.setPlanetCode(planetCode);
        boolean save = this.save(user);
        if(!save){
            throw new BuessisException(ErrorCode.SYSTEM_ERROR, "注册保存失败");
        }
        return user.getId();

    }

    /**
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return 用户信息
     */

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BuessisException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        this.correctAccountAndPassword(userAccount, userPassword);
        QueryWrapper<User> Wrapper = new QueryWrapper<User>();
        Wrapper.eq("userAccount",userAccount);
        User user = userMapper.selectOne(Wrapper);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(user==null){
            log.info("用户不存在");

            throw new BuessisException(ErrorCode.PARAMS_ERROR, "用户账号不存在");
        }
        if(encoder.matches(userPassword,user.getUserPassword())){
            User safetyuser = getSafetyUser(user);
            request.getSession().setAttribute(USER_LOGIN_STATE,safetyuser);
            return safetyuser;
        }
        log.info("密码错误");
        throw new BuessisException(ErrorCode.PARAMS_ERROR, "用户密碼錯誤");
    }

    /**
     *
     * @param userAccount
     * @return 用户列表
     */

    @Override
    public List<User> searchuser(String userAccount) {
        QueryWrapper<User> Wrapper = new QueryWrapper<User>();
        if(userAccount!=null) {
            Wrapper.like("userAccount", userAccount);
        }
        List<User> userlist = this.list(Wrapper);
        return userlist.stream().map(user -> getSafetyUser(user)).collect(Collectors.toList());
    }

    /**
     *
     * @param OriginUser
     * @return 脱敏·用户信息
     */
    @Override
    public User getSafetyUser(User OriginUser){
        if(OriginUser==null){
            return null;
        }
        User safetyuser = new User();
        safetyuser.setId(OriginUser.getId());
        safetyuser.setUsername(OriginUser.getUsername());
        safetyuser.setUserAccount(OriginUser.getUserAccount());
        safetyuser.setAvatarUrl(OriginUser.getAvatarUrl());
        safetyuser.setGender(OriginUser.getGender());
        safetyuser.setPhone(OriginUser.getPhone());
        safetyuser.setEmail(OriginUser.getEmail());
        safetyuser.setUserStatus(OriginUser.getUserStatus());
        safetyuser.setCreateTime(OriginUser.getCreateTime());
        safetyuser.setUserRole(OriginUser.getUserRole());
        safetyuser.setPlanetCode(OriginUser.getPlanetCode());
        safetyuser.setTags(OriginUser.getTags());
      return safetyuser;
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

//    /**
//     * 用SQL进行模糊查询
//     * @param tagNameList
//     * @return
//     */
//    @Deprecated
//   private   List<User> searchUsersByTags(List<String> tagNameList) {
//        if (CollectionUtils.isEmpty(tagNameList)) {
//            throw new BuessisException(ErrorCode.PARAMS_ERROR);
//        }
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        for (String tagName : tagNameList) {
//            queryWrapper.like("tags", tagName);
//        }
//        List<User> results = userMapper.selectList(queryWrapper);
//        return results.stream().map(this::getSafetyUser).collect(Collectors.toList());
//    }

    /**
     * 用内存进行查询
     * @param tagNameList
     * @return
     */
    @Override
    public List<User> searchUsersByTags(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BuessisException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        return userList.parallelStream().filter(user -> {
            String tags = user.getTags();
            Set<String> tagSet = gson.fromJson(tags, new TypeToken<Set<String>>() {
            }.getType());
            tagSet = Optional.ofNullable(tagSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if (!tagSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(user -> getSafetyUser(user)).collect(Collectors.toList());
    }

    @Override
    public int updateUser(User user,User loginUser) {
      long updateuserid = user.getId();
      if(updateuserid <= 0) {
          throw new BuessisException(ErrorCode.PARAMS_ERROR);
      }
      if(loginUser.getUserRole()!= ADMIN_ROLE && updateuserid != loginUser.getId()) {
          throw new BuessisException(ErrorCode.NO_AUTH, "没有权限");
      }
      User oldUser = this.getById(updateuserid);
      if(oldUser == null) {
          throw new BuessisException(ErrorCode.NULL_ERROR, "用户不存在");
      }

        return userMapper.updateById(user);


    }
    @Override
    public boolean isAdmin(HttpServletRequest request)
    {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser   == null || currentUser.getUserRole() != ADMIN_ROLE)
            return false;
        return true;
    }

    @Override
    public User getloginuser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User loginUser = (User) userObj;
        if(loginUser==null){
            throw  new BuessisException(ErrorCode.NOT_LOGIN);
        }
        return loginUser;
    }
    @Override
    public void  correctAccountAndPassword(String userAccount, String userPassword){
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BuessisException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        if(userAccount.length()<4){
            throw new BuessisException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if(userPassword.length()<8){
            throw new BuessisException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BuessisException(ErrorCode.PARAMS_ERROR, "用户账号不合法");

        }
    }

    @Override
    public long updatePassword(long id, String oldPassword, String newPassword) {
        if(StringUtils.isAnyBlank(oldPassword,newPassword)){
            throw new BuessisException(ErrorCode.NULL_ERROR, "参数为空");
        }

        if(newPassword.length()<8){
            throw new BuessisException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if(id<=0) {
            throw new BuessisException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        User olduser=  this.getById(id);
        if(olduser==null){
            throw new BuessisException(ErrorCode.NULL_ERROR, "用户不存在");
        }
        if(olduser.getUserRole()!= ADMIN_ROLE && id != olduser.getId()) {
            throw new BuessisException(ErrorCode.NO_AUTH, "没有权限");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(oldPassword,olduser.getUserPassword())==false){
           throw new BuessisException(ErrorCode.PARAMS_ERROR, "旧密码错误");
        }
        String bcryptPassword = encoder.encode(newPassword);
        olduser.setUserPassword(bcryptPassword);
        userMapper.updateById(olduser);
        return  olduser.getId();
    }
}




