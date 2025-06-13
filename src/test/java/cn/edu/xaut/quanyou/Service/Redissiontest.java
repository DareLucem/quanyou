package cn.edu.xaut.quanyou.Service;

import cn.edu.xaut.quanyou.Model.User;
import cn.edu.xaut.quanyou.config.RedissionConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@SpringBootTest
@Slf4j
public class Redissiontest {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private RedissonClient redissionClient;
    @Autowired
    UserService userService;
    private List<Long> mian= Arrays.asList(1L,8L);
    //    @Test
//    public void test() {
//        RList<String> list = redission.getList("list");
//        list.add("name");
//        System.out.println(list.get(0));
//        RMap<String, String> map = redission.getMap("map");
//        map.put("name", "quanyou");
//        System.out.println(map.get("name"));
//
//
//    }
    @Test
    public void testwatchdog() {
        RLock lock = redissionClient.getLock("quanyou:job:precachejob:lock");
        try {
            if (lock.tryLock(0, -1L, TimeUnit.MILLISECONDS)) {
                log.info("获取锁成功");
                for (Long id : mian) {
                    ValueOperations<String, Object> valueObject = redisTemplate.opsForValue();
                    QueryWrapper queryWrapper = new QueryWrapper<>();
                    IPage<User> page = userService.page(new Page<>(1, 10), queryWrapper);
                    String rediskey = String.format("quanyou:user:recommend_%s", id);
                    try {
                        valueObject.set(rediskey, page, 82800000);  //23小时过期
                    } catch (Exception e) {
                        log.info("redis set key error:" + e.getMessage());
                    }

                }
            } else {
                log.info("获取锁失败");
            }
        } catch (Exception e) {
            log.error("获取锁异常");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                log.info("释放锁成功");
                lock.unlock();
            }
        }
    }
}
