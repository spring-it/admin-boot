package cn.mesmile.admin.common.sms.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zb
 * @Description
 */
@Data
public class SmsData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;

    /**
     * 短信匹配参数
     */
    private Map<String, String> params;

}
