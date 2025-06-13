package cn.edu.xaut.quanyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@MapperScan("cn.edu.xaut.quanyou.Mapper")
@EnableScheduling

public class quanyouApplication {

    public static void main(String[] args) {
        SpringApplication.run(quanyouApplication.class, args);
    }

}
