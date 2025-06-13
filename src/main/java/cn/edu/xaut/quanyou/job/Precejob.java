package cn.edu.xaut.quanyou.job;

import cn.edu.xaut.quanyou.Model.User;
import cn.edu.xaut.quanyou.Service.UserService;
import cn.edu.xaut.quanyou.Untils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Precejob {

    @Resource
    private UserService userService;
    @Resource
   private RedisUtil  redisUtil;
    @Resource
    private RedissonClient redissionClient;
    private List<Long> mian=Arrays.asList(1L,8L);
   @Scheduled(cron = "00 19 18 * * ?")
    public void doprecejob(){
       RLock lock= redissionClient.getLock("quanyou:job:precachejob:lock");
       try {
           if(lock.tryLock(0,30000L,  TimeUnit.MILLISECONDS)){
               log.info("获取锁成功");
               for (Long id:mian){

                   QueryWrapper queryWrapper=new QueryWrapper<>();
                   IPage<User> page =userService.page(new Page<>(1,10),queryWrapper);
                   String rediskey=String.format("quanyou:user:recommend_%s",id);
                   try {
                      redisUtil.set(rediskey,page,3600000,TimeUnit.MILLISECONDS);  //1小时过期
                   } catch (Exception e) {
                       log.info("redis set key error:" + e.getMessage());
                   }

               }
           }else {
               log.info("获取锁失败");
           }
       } catch (Exception e) {
           log.error("获取锁异常");
       } finally {
           if(lock.isHeldByCurrentThread()){
               log.info("释放锁成功");
               lock.unlock();
           }
       }
//        java -jar .\quanyou-0.0.1-SNAPSHOT.jar --server.prot=8081


   }
}
