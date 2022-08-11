package cn.mesmile.admin.modules.message.vo;

import lombok.Data;

/**
 * @author zb
 * @Description
 */
@Data
public class MqMessageVO {

    /**
     * 接收人id
     */
    private String receiveUser;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 标题
     */
    private String title;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 发送日志id
     */
    private Long messageLogId;
}
