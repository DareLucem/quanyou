package cn.edu.xaut.quanyou.Controller;

import cn.edu.xaut.quanyou.DTO.UserLoginRequest;
import cn.edu.xaut.quanyou.DTO.UserRegisterRequest;
import cn.edu.xaut.quanyou.DTO.UserUpdatepswRequest;
import cn.edu.xaut.quanyou.Exception.BuessisException;
import cn.edu.xaut.quanyou.Model.User;
import cn.edu.xaut.quanyou.Service.UserService;
import cn.edu.xaut.quanyou.Untils.AliOSSUtils;
import cn.edu.xaut.quanyou.common.BaseResponse;
import cn.edu.xaut.quanyou.common.ErrorCode;
import cn.edu.xaut.quanyou.common.ResultUntil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.xaut.quanyou.Contant.UserContant.ADMIN_ROLE;
import static cn.edu.xaut.quanyou.Contant.UserContant.USER_LOGIN_STATE;
//@Api(tags = "测试模块")
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000"},allowCredentials = "true")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;
    @Autowired
    AliOSSUtils aliOSSUtils;

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
         User loginuser = userService.getloginuser(request);
         long userId =loginuser.getId();
         User user = userService.getById(userId);
         User safetyUser = userService.getSafetyUser(user);
         return ResultUntil.success(safetyUser);
     }

    @GetMapping("/search")
    public BaseResponse<List<User>>searchUser(String userAccount, HttpServletRequest request)
    {
        if(!userService.isAdmin(request)&&userService.getloginuser(request)==null)
     {
         throw  new BuessisException(ErrorCode.NO_AUTH,"缺少管理员权限");
       }
        List<User> list = userService.searchuser(userAccount);
        return ResultUntil.success(list);
    }
    @GetMapping("/search/tags")
    public BaseResponse<Page<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList,@RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "10") int pageSize){
        if(CollectionUtils.isEmpty(tagNameList))
            throw new BuessisException(ErrorCode.PARAMS_ERROR);
        return ResultUntil.success(userService.searchUsersByTags(tagNameList,pageNum,pageSize));
    }
    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user,HttpServletRequest request){
        if(user==null) {
            throw new BuessisException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getloginuser(request);
        int result = userService.updateUser(user,loginUser);
            return ResultUntil.success(result);
        }
        @GetMapping("recommend")
        public BaseResponse<IPage<User>> recommendUsers(@RequestParam(defaultValue = "8") Long  PageSize, @RequestParam(defaultValue = "1") Long PageNum, HttpServletRequest request)
            {
            if(userService.getloginuser(request)==null)
            {
                throw  new BuessisException(ErrorCode.NO_AUTH,"用户未登录");
            }
                User loginuser = userService.getloginuser(request);
            return  ResultUntil.success(userService.recommendUsers(PageSize,PageNum,loginuser));
        }
    @PostMapping  ("/delete")
    public BaseResponse<Boolean> delete(long id, HttpServletRequest request)
    {
        if(!userService.isAdmin(request))
        {
            throw  new BuessisException(ErrorCode.NO_AUTH,"缺少管理员权限");
        }
        if (id <= 0) {
            throw new BuessisException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(id);
        return  ResultUntil.success(result);
    }
    /**
     * 上传头像
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/upload/avatar")
    public  BaseResponse<String> uploadAvatar(@RequestParam("file") MultipartFile file,HttpServletRequest request) {
        User loginUser = userService.getloginuser(request);
        if (loginUser == null) {
            throw new BuessisException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        String url= null;
        try {
            url = aliOSSUtils.upload(file);
            log.info("上传成功，图片路径为{}",url);
        } catch (IOException e) {
            throw new BuessisException(ErrorCode.SYSTEM_ERROR,"上传失败");
        }
        if (url == null)
            throw new BuessisException(ErrorCode.SYSTEM_ERROR,"上传失败");
        return  ResultUntil.success(url);
    }
    @PostMapping  ("/updatePassword")
    public BaseResponse<Long> updatePassword(@RequestBody UserUpdatepswRequest userUpdatepswRequest,HttpServletRequest request){
        if(userUpdatepswRequest==null) {
            throw new BuessisException(ErrorCode.PARAMS_ERROR);
        }
        if(!userService.isAdmin(request)&&userService.getloginuser(request)==null)
        {
            throw  new BuessisException(ErrorCode.NO_AUTH,"缺少管理员权限");
        }
        long    userid = userUpdatepswRequest.getId();
        String  oldPassword = userUpdatepswRequest.getOldPassword();
        String  newPassword = userUpdatepswRequest.getNewPassword();

        return ResultUntil.success(userService.updatePassword(userid,oldPassword,newPassword));
    }

     @PostMapping  ("/logout")
     public BaseResponse<Integer> userLogout(HttpServletRequest request)
     {if(request==null){
         throw  new BuessisException(ErrorCode.NULL_ERROR);
     }
         int result = userService.userLogout(request);
         return ResultUntil.success(result);
     }

}
