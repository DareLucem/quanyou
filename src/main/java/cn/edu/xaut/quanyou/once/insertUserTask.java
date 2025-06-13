package cn.edu.xaut.quanyou.once;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import cn.edu.xaut.quanyou.Mapper.UserMapper;
import cn.edu.xaut.quanyou.Model.User;
import cn.edu.xaut.quanyou.Service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class insertUserTask {
    @Resource
    UserService userService;

    private static final long MAX_VALUE=100000;
//    @PostConstruct
//    @Scheduled(initialDelay =2000,  fixedDelay = Long.MAX_VALUE)
//    public void insertUser() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        List<User> userList = new ArrayList<>();
//        for (int i = 0; i <=MAX_VALUE; i++){
//            User  user = new User();
//            user.setUsername("假圈友");
//            user.setUserAccount("fakeuser"+i);
//            user.setAvatarUrl("https://springlearningcyt-web-ts.oss-cn-beijing.aliyuncs.com/05acf385-4305-477c-8d39-6792e81a5a78.jpg");
//            user.setGender(i%2);
//            user.setUserPassword("123456789");
//            user.setPhone("11111");
//            user.setEmail("1111@qq.com");
//            user.setPlanetCode("0000"+i);
//            user.setTags("[\"大一\",\"java\"]");
//           userList.add(user);
//        }
//        userService.saveBatch(userList,10000);
//        stopWatch.stop();
//        System.out.println(stopWatch.getTotalTimeMillis());
//    }

//    @Scheduled(initialDelay =2000,  fixedDelay = Long.MAX_VALUE)
    public void doconcurrenyinsertUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<CompletableFuture<Void>> futureList = new ArrayList<>();

        int j=0;
        for (int i= 0; i <10; i++){
            List<User> userList = new ArrayList<>();
            while(true) {
                j++;
                User user = new User();
                user.setUsername("假圈友");
                user.setUserAccount("fakeuser" +j);
                user.setAvatarUrl("https://springlearningcyt-web-ts.oss-cn-beijing.aliyuncs.com/05acf385-4305-477c-8d39-6792e81a5a78.jpg");
                user.setGender(i % 2);
                user.setUserPassword("123456789");
                user.setPhone("11111");
                user.setEmail("1111@qq.com");
                user.setPlanetCode("0000" +j);
                user.setTags("[\"大一\",\"java\"]");
                userList.add(user);
                if (j%10000==0){
                    break;
                }

            }CompletableFuture <Void> future =CompletableFuture.runAsync(() -> {
                System.out.println("threadname"+Thread.currentThread().getName());
                userService.saveBatch(userList,10000);
            });
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
