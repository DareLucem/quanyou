package cn.edu.xaut.quanyou.Controller;

import cn.edu.xaut.quanyou.DTO.*;
import cn.edu.xaut.quanyou.Exception.BuessisException;
import cn.edu.xaut.quanyou.Model.Team;
import cn.edu.xaut.quanyou.Model.User;
import cn.edu.xaut.quanyou.Service.TeamService;
import cn.edu.xaut.quanyou.Service.UserService;
import cn.edu.xaut.quanyou.Untils.AliOSSUtils;
import cn.edu.xaut.quanyou.common.BaseResponse;
import cn.edu.xaut.quanyou.common.ErrorCode;
import cn.edu.xaut.quanyou.common.ResultUntil;
import cn.edu.xaut.quanyou.vo.TeamUserVo;
import cn.edu.xaut.quanyou.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/team")
@CrossOrigin(origins = {"http://localhost:3000"},allowCredentials = "true")
@Slf4j
public class TeamController {
    @Resource
    private UserService userService;
    @Resource
    private TeamService TeamService;
    @Autowired
    AliOSSUtils aliOSSUtils;
    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamaddRequest teamaddRequest, HttpServletRequest request){
        if(teamaddRequest==null) {
            throw new BuessisException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getloginuser(request);
        if (loginUser == null) {
            throw new BuessisException(ErrorCode.NOT_LOGIN);
        }
        return ResultUntil.success(TeamService.addTeam(teamaddRequest, loginUser));
    }
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamupdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BuessisException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getloginuser(request);
        boolean result = TeamService.updateTeam(teamUpdateRequest, loginUser);
        if (!result) {
            throw new BuessisException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUntil.success(result);
    }
    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(long id) {
        if (id <= 0) {
            throw new BuessisException(ErrorCode.PARAMS_ERROR);
        }
        Team team = TeamService.getById(id);
        if (team == null) {
            throw new BuessisException(ErrorCode.NULL_ERROR);
        }
        return ResultUntil.success(team);
    }
    @PostMapping("/list")
    public BaseResponse<List<TeamUserVo>> listTeams(@RequestBody TeamQuery teamQuery, HttpServletRequest request) {
       boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVo> teamList = TeamService.listTeams(teamQuery,isAdmin);
        return ResultUntil.success(teamList);
    }
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamspge(TeamQuery teamQuery, HttpServletRequest request) {
        if(teamQuery==null)
        {
            throw new BuessisException(ErrorCode.PARAMS_ERROR);
        }
        if(userService.getloginuser( request)==null){
            throw new BuessisException(ErrorCode.NO_AUTH);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> page = TeamService.page(new Page<Team>(teamQuery.getPageNum(), teamQuery.getPageSize()), queryWrapper);
        return ResultUntil.success(page);
    }
    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
    if(teamJoinRequest== null){
        throw new BuessisException(ErrorCode.PARAMS_ERROR);
    }
    User loginUser = userService.getloginuser(request);
    return ResultUntil.success(TeamService.joinTeam(teamJoinRequest, loginUser));
}
@PostMapping("/info")
    public BaseResponse<TeamUserVo> updateTeam(@RequestBody TeamQuery teamQuery, HttpServletRequest request) {
    if(teamQuery== null){
        throw new BuessisException(ErrorCode.PARAMS_ERROR);
    }
    User loginUser = userService.getloginuser(request);
    return ResultUntil.success(TeamService.infoteam(teamQuery, loginUser));
}
@PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request)
{
    if(teamQuitRequest== null){
        throw new BuessisException(ErrorCode.PARAMS_ERROR);
    }
    User loginUser = userService.getloginuser(request);
    return ResultUntil.success(TeamService.quitTeam(teamQuitRequest, loginUser));

}
@PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody TeamDeleteRequest teamDeleteRequest, HttpServletRequest request){
    User loginuser = userService.getloginuser(request);
    if(!userService.isAdmin(request)&&loginuser==null)
        {
            throw new BuessisException(ErrorCode.NO_AUTH,"缺少管理员权限");
        }
   Boolean result = TeamService.removeteam(teamDeleteRequest.getId(),loginuser);

        if(!result)
        {
            throw new BuessisException(ErrorCode.SYSTEM_ERROR,"删除失败");
        }
        return ResultUntil.success(result);
};
    @PostMapping("/list/my/join")
    public BaseResponse<List<TeamUserVo>> listMyJoinTeams(HttpServletRequest request) {
        User loginUser = userService.getloginuser(request);
        if (loginUser == null) {
            throw new BuessisException(ErrorCode.NOT_LOGIN);
        }
        List<TeamUserVo> teamList = TeamService.listMyJoinTeams(loginUser.getId());
        return ResultUntil.success(teamList);
    }
    @GetMapping("/getmessages")
    public BaseResponse<String> getMessages(Long teamId,HttpServletRequest request) {
        User loginUser = userService.getloginuser(request);
        if (loginUser == null) {
            throw new BuessisException(ErrorCode.NOT_LOGIN);
        }
        String messages = TeamService.getmessage(teamId,loginUser);
        return ResultUntil.success(messages);
    }





}
