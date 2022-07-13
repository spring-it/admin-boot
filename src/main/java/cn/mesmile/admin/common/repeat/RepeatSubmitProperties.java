package cn.mesmile.admin.common.repeat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zb
 * @Description
 */
@Data
@ConfigurationProperties("repeat.submit")
public class RepeatSubmitProperties {

    /**
     * 是否开启重复检查，注解，默认 false
     */
    private Boolean enabled = Boolean.FALSE;
    /**
     * 放行路径
     */
    private List<String> skipUrl = new ArrayList<>();

}