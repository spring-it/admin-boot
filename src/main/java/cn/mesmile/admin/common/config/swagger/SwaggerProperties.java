package cn.mesmile.admin.common.config.swagger;

import cn.mesmile.admin.common.constant.AdminConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Swagger配置类
 *
 * @author zb
 * @Description
 */
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    private List<String> basePackages = new ArrayList(Collections.singletonList(AdminConstant.BASE_PACKAGE));
    private List<String> basePath = new ArrayList();
    private List<String> excludePath = new ArrayList();
    private String title = "Admin 接口文档系统";
    private String description = "Admin 接口文档系统";
    private String version = "1.0.0.RELEASE";
    private String license = "Powered By Admin";
    private String licenseUrl = "https://www.mesmile.cn";
    private String termsOfServiceUrl = "https://www.mesmile.cn";
    private String host = "";
    private SwaggerProperties.Contact contact = new SwaggerProperties.Contact();
    private SwaggerProperties.Authorization authorization = new SwaggerProperties.Authorization();

    public List<String> getBasePackages() {
        return this.basePackages;
    }

    public List<String> getBasePath() {
        return this.basePath;
    }

    public List<String> getExcludePath() {
        return this.excludePath;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getVersion() {
        return this.version;
    }

    public String getLicense() {
        return this.license;
    }

    public String getLicenseUrl() {
        return this.licenseUrl;
    }

    public String getTermsOfServiceUrl() {
        return this.termsOfServiceUrl;
    }

    public String getHost() {
        return this.host;
    }

    public SwaggerProperties.Contact getContact() {
        return this.contact;
    }

    public SwaggerProperties.Authorization getAuthorization() {
        return this.authorization;
    }

    public void setBasePackages(final List<String> basePackages) {
        this.basePackages = basePackages;
    }

    public void setBasePath(final List<String> basePath) {
        this.basePath = basePath;
    }

    public void setExcludePath(final List<String> excludePath) {
        this.excludePath = excludePath;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public void setLicense(final String license) {
        this.license = license;
    }

    public void setLicenseUrl(final String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public void setTermsOfServiceUrl(final String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public void setContact(final SwaggerProperties.Contact contact) {
        this.contact = contact;
    }

    public void setAuthorization(final SwaggerProperties.Authorization authorization) {
        this.authorization = authorization;
    }

    @Data
    public static class AuthorizationApiKey {
        private String name = "";
        private String keyName = "";
        private String passAs = "";
    }

    @Data
    public static class AuthorizationScope {
        private String name = "";
        private String scope = "";
        private String description = "";
    }

    public static class Authorization {
        private String name = "";
        private String authRegex = "^.*$";
        private List<SwaggerProperties.AuthorizationScope> authorizationScopeList = new ArrayList();
        private List<SwaggerProperties.AuthorizationApiKey> authorizationApiKeyList = new ArrayList();
        private List<String> tokenUrlList = new ArrayList();

        public String getName() {
            return this.name;
        }

        public String getAuthRegex() {
            return this.authRegex;
        }

        public List<SwaggerProperties.AuthorizationScope> getAuthorizationScopeList() {
            return this.authorizationScopeList;
        }

        public List<SwaggerProperties.AuthorizationApiKey> getAuthorizationApiKeyList() {
            return this.authorizationApiKeyList;
        }

        public List<String> getTokenUrlList() {
            return this.tokenUrlList;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public void setAuthRegex(final String authRegex) {
            this.authRegex = authRegex;
        }

        public void setAuthorizationScopeList(final List<SwaggerProperties.AuthorizationScope> authorizationScopeList) {
            this.authorizationScopeList = authorizationScopeList;
        }

        public void setAuthorizationApiKeyList(final List<SwaggerProperties.AuthorizationApiKey> authorizationApiKeyList) {
            this.authorizationApiKeyList = authorizationApiKeyList;
        }

        public void setTokenUrlList(final List<String> tokenUrlList) {
            this.tokenUrlList = tokenUrlList;
        }

    }

    @Data
    public static class Contact {
        private String name = "admin";
        private String url = "https://gitee.com/springzb/admin-boot";
        private String email = "admin@163.com";
    }
}
