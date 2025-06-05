package cn.edu.xaut.quanyou.Controller;

import cn.edu.xaut.quanyou.DTO.UserLoginRequest;
import cn.edu.xaut.quanyou.DTO.UserRegisterRequest;
import cn.edu.xaut.quanyou.Exception.BuessisException;
import cn.edu.xaut.quanyou.Model.User;
import cn.edu.xaut.quanyou.Service.UserService;
import cn.edu.xaut.quanyou.common.BaseResponse;
import cn.edu.xaut.quanyou.common.ErrorCode;
import cn.edu.xaut.quanyou.common.ResultUntil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.xaut.quanyou.Contant.UserContant.ADMIN_ROLE;
import static cn.edu.xaut.quanyou.Contant.UserContant.USER_LOGIN_STATE;
//@Api(tags = "测试模块")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null)
                throw  new BuessisException(ErrorCode.NULL_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode))
           throw new BuessisException(ErrorCode. PARAMS_ERROR);
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUntil.success(result);

    }

    /**
     * 用户登陆
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request)
    {
        if(userLoginRequest == null)
            throw  new BuessisException(ErrorCode.NULL_ERROR);
        String userAccount =   userLoginRequest.getUserAccount();
        String userPassword =  userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword))
            throw new BuessisException(ErrorCode. PARAMS_ERROR);
        User result  = userService.userLogin(userAccount, userPassword, request);
        return ResultUntil.success(result);
    }
     @GetMapping("/current")
     public BaseResponse<User> getCurrentUser(HttpServletRequest request)
     {
         Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
         User currentUser = (User) userObj;
         if(currentUser==null){
             throw  new BuessisException(ErrorCode.NOT_LOGIN);
         }
         long userId = currentUser.getId();
         // TODO 校验用户是否合法
         User user = userService.getById(userId);
         User safetyUser = userService.getSafetyUser(user);
         return ResultUntil.success(safetyUser);
     }

    @GetMapping("/search")
    public BaseResponse<List<User>>searchUser(String userAccount, HttpServletRequest request)
    {   if(!isAdmin(request))
     {
         throw  new BuessisException(ErrorCode.NO_AUTH,"缺少管理员权限");
       }
        List<User> list = userService.searchuser(userAccount);
        return ResultUntil.success(list);
    }
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList){
        if(CollectionUtils.isEmpty(tagNameList))
            throw new BuessisException(ErrorCode.PARAMS_ERROR);
        return ResultUntil.success(userService.searchUsersByTags(tagNameList));
    }
    @PostMapping  ("/delete")
    public BaseResponse<Boolean> delete(long id, HttpServletRequest request)
    {
        if(!isAdmin(request))
        {
            throw  new BuessisException(ErrorCode.NO_AUTH,"缺少管理员权限");
        }
        if (id <= 0) {
            throw new BuessisException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(id);
        return  ResultUntil.success(result);
    }
     @PostMapping  ("/logout")
     public BaseResponse<Integer> userLogout(HttpServletRequest request)
     {if(request==null){
         throw  new BuessisException(ErrorCode.NULL_ERROR);
     }
         int result = userService.userLogout(request);
         return ResultUntil.success(result);
     }
    public boolean isAdmin(HttpServletRequest request)
    {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null || currentUser.getUserRole() != ADMIN_ROLE)
            return false;
        return true;
    }
}
