package cn.edu.xaut.quanyou.Untils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="aliyun.oss")
public class AliossProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName ;
}
