package cn.mesmile.admin.common.i18n;

import cn.hutool.core.util.StrUtil;
import cn.mesmile.admin.common.constant.AdminConstant;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 *
 * @author zb
 * @description 自定义 语言解析器
 */
public class MyLocaleResolver implements LocaleResolver {

    /**
     * 自定义国际化解析，优先以 链接参数为准
     * 然后 Accept-Language 请求头为二级优先
     */
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        int languageArraySize = 2;
        // 从请求头上获取
        Locale locale = request.getLocale();
        //获取请求中的语言参数  admin_language
        String language = request.getParameter(AdminConstant.ADMIN_LANGUAGE);
        //如果请求的链接中携带了 国际化的参数
        if (StrUtil.isNotEmpty(language)){
            // zh-CN   en-US
            String[] languageArray = language.split("-");
            if (languageArray.length == languageArraySize){
                // 国家 地区
                locale = new Locale(languageArray[0], languageArray[1]);
            }
        }
        return locale == null ? Locale.getDefault() : locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse httpServletResponse, Locale locale) {

    }
}