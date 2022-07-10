package cn.mesmile.admin.common.config.swagger;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ApiKey;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Swagger工具类
 *
 * @author zb
 * @Description
 */
public class SwaggerUtil {

    public static Predicate<RequestHandler> basePackages(final List<String> basePackages) {
        return (input) -> {
            return (Boolean) declaringClass(input).transform(handlerPackage(basePackages)).or(true);
        };
    }

    private static Function<Class<?>, Boolean> handlerPackage(final List<String> basePackages) {
        return (input) -> {
            Iterator iterator = basePackages.iterator();
            boolean isMatch;
            do {
                if (!iterator.hasNext()) {
                    return false;
                }
                String strPackage = (String) iterator.next();
                isMatch = input.getPackage().getName().startsWith(strPackage);
            } while (!isMatch);
            return true;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

    public static ApiKey clientInfo() {
        return new ApiKey("ClientInfo", "Authorization", "header");
    }

    public static ApiKey adminAuth() {
        return new ApiKey("AdminAuth", "Admin-Auth", "header");
    }

    public static ApiKey adminTenant() {
        return new ApiKey("TenantId", "Tenant-Id", "header");
    }
}