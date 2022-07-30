package cn.mesmile.admin.common.sms.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zb
 * @Description
 */
@NoArgsConstructor
@Data
public class SmsResponse  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 返回码
     */
    private Integer code;
    /**
     * 提示消息
     */
    private String msg;

    public SmsResponse(boolean success,Integer code,String msg){
        this.success = success;
        this.code = code;
        this.msg = msg;
    }
}
