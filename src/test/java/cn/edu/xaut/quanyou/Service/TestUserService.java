package cn.edu.xaut.quanyou.Service;


import cn.edu.xaut.quanyou.Model.User;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.xml.ws.Service;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestUserService {
    @Autowired
    UserService userService;
    @Test
    public void testuserRegister()
    {
         long  result=userService.userRegister("cai io","1234567890","1234567890","1");
        Assert.isTrue(result==-1,"注册失败");
    }
    @Test
    public void testuserserachbytags()
        {
            List<User> result=userService.searchUsersByTags(Arrays.asList("java","python"));
            Assert.isTrue(result.size()==1,"搜索成功");
        }

}
