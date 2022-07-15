package cn.mesmile.admin.common.oss.enums;

/**
 * @author zb
 * @Description
 */
public enum  PolicyTypeEnum {

    /**
     * 权限
     */
    READ("read", "只读"),
    WRITE("write", "只写"),
    READ_WRITE("read_write", "读写");

    private final String type;
    private final String policy;

    public String getType() {
        return this.type;
    }

    public String getPolicy() {
        return this.policy;
    }

    private PolicyTypeEnum(final String type, final String policy) {
        this.type = type;
        this.policy = policy;
    }
}
