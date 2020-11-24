package com.highershine.portal.config;

import com.highershine.portal.common.constants.CommonConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: swagger 配置
 * @Author: mizhanlei
 * @Date: 2019/12/17 14:51
 */
@Configuration
@EnableSwagger2
@Profile({"dev", "test"})
public class SwaggerConfig {
    @Value("${server.servlet.context-path}")
    private String contextPath;
    /**
     * 创建Rest Api描述
     * @return
     */
    @Bean
    public Docket createRestApi() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(401).message("未经授权")
                .responseModel(new ModelRef(CommonConstant.MODELREF_RESULT)).build());
        responseMessageList.add(new ResponseMessageBuilder().code(404).message("找不到资源")
                .responseModel(new ModelRef(CommonConstant.MODELREF_RESULT)).build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("服务器内部错误")
                .responseModel(new ModelRef(CommonConstant.MODELREF_RESULT)).build());

        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .apiInfo(apiInfo())
                .select()                       //按条件指定生成文档接口
                .paths(PathSelectors.any())
                .build();
    }
    /**
     * 接口描述
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("门户网站")
                .description("门户网站\t" + contextPath)
                .version("1.0")
                .build();
    }
}
