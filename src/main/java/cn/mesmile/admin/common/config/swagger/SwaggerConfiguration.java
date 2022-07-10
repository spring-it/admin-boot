package cn.mesmile.admin.common.config.swagger;

import cn.mesmile.admin.common.constant.AdminConstant;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Swagger配置类
 *
 * @author zb
 * @Description
 */
@EnableConfigurationProperties({SwaggerProperties.class})
@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
@RequiredArgsConstructor
public class SwaggerConfiguration {

    /**
     * 引入swagger配置类
     */
    private final SwaggerProperties swaggerProperties;

    /**
     * 引入Knife4j扩展类
     */
    private final OpenApiExtensionResolver openApiExtensionResolver;


    @Bean
    public Docket sysDocket() {
        return docket("系统模块",
                Arrays.asList(AdminConstant.BASE_PACKAGE + ".admin.modules.system"));
    }

//	@Bean
//	public Docket flowDocket() {
//		return docket("工作流模块", Collections.singletonList(AdminConstant.BASE_PACKAGE + ".flow"));
//	}

    private Docket docket(String groupName, List<String> basePackages) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .apiInfo(apiInfo())
//			.ignoredParameterTypes(AdminUser.class)
                .select()
                .apis(SwaggerUtil.basePackages(basePackages))
                .paths(PathSelectors.any())
                .build().securityContexts(securityContexts()).securitySchemes(securitySchemas())
                .extensions(openApiExtensionResolver.buildExtensions(groupName));
    }

    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^.*$"))
                .build());
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverywhere");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference(SwaggerUtil.clientInfo().getName(), authorizationScopes),
                new SecurityReference(SwaggerUtil.adminAuth().getName(), authorizationScopes),
                new SecurityReference(SwaggerUtil.adminTenant().getName(), authorizationScopes));
    }

    private List<SecurityScheme> securitySchemas() {
        return Lists.newArrayList(SwaggerUtil.clientInfo(), SwaggerUtil.adminAuth(), SwaggerUtil.adminTenant());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }

}
