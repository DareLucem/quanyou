package cn.edu.xaut.quanyou.job;

import cn.edu.xaut.quanyou.Model.User;
import cn.edu.xaut.quanyou.Service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
@Slf4j
@Component
public class Precejob {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String,Object>  redisTemplate;
    private List<Integer> mian=Arrays.asList(1,8);
   @Scheduled(cron = "0 00 01 * * ?")
    public void doprecejob(){

       for (Integer id:mian){
           ValueOperations<String,Object> valueObject=redisTemplate.opsForValue();
           QueryWrapper queryWrapper=new QueryWrapper<>();
           IPage<User> page =userService.page(new Page<>(1,10),queryWrapper);
           String rediskey=String.format("quanyou:user:recommend_%s",id);
           try {
               valueObject.set(rediskey,page,82800000);  //23小时过期
           } catch (Exception e) {
               log.info("redis set key error:" + e.getMessage());
           }

       }
    }
}
