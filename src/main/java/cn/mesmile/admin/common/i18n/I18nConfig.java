package cn.mesmile.admin.common.i18n;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author zb
 * @Description
 */
@Configuration
public class I18nConfig {

    @Bean
    public LocaleResolver localeResolver(){
        return new MyLocaleResolver();
    }

}
