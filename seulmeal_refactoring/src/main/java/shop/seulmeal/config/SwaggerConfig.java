package shop.seulmeal.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig {

    private final String version = "v1";

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Seulmeal API")
                .description("> **슬밀 프로젝트(커스터마이징 밀키트몰)**<br><br> : 밀키트의 재료 중 제외 또는 추가가 가능하며,<br><br> &nbsp;&nbsp;레시피를 공유하는 커뮤니티를 통해 소통 가능합니다.")
                .build();
    }

    @Bean
    public Docket apiV1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(version)
                .useDefaultResponseMessages(false)
                .apiInfo(this.apiInfo())
                .tags(new Tag("PurchaseRestController", ""))
                .select()
                .apis(RequestHandlerSelectors.basePackage("shop.seulmeal.web.purchase"))
                .paths(PathSelectors.ant("/api/v1/purchase/**"))
                .build();
    }
}