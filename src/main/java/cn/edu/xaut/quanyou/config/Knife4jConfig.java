//package cn.edu.xaut.quanyou.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
//
///**
// * Knife4j配置类
// */
//@Configuration
//@EnableSwagger2WebMvc
//@Profile({"dev", "test"})
//public class Knife4jConfig {
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(new ApiInfoBuilder()
//                        // 网站标题
//                        .title("Dare的Swagger文档")
//                        // 描述 可以穿插md语法
//                        .description("# 这是描述！")
//                        // 服务条款
//                        .termsOfServiceUrl("......")
//                        // 设置作者 服务器url 邮箱
//                        .contact(new Contact("Dare", "http://localhost:9999/demo", "xxx"))
//                        // 许可证
//                        .license("...")
//                        // 许可证url
//                        .licenseUrl("....")
//                        // 版本
//                        .version("1.0")
//                        .build())
//                .groupName("test测试组")
//                .select()
//                // 要扫描的包
//                .apis(RequestHandlerSelectors.basePackage("cn.edu.xaut.quanyou.Controller"))
//                // 要扫描的url
//                .paths(PathSelectors.any())
//                .build();
//    }
//}
