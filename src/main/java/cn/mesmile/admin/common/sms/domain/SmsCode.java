package cn.mesmile.admin.common.sms.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zb
 * @Description
 */
@Data
public class SmsCode implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 是否发送成功
     */
    private boolean success = true;
    /**
     * 电话号码
     */
    private String phone;

    private String id;

    private String value;

}
