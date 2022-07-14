package cn.mesmile.admin.common.oss;

/**
 * @author zb
 * @Description
 */
public interface OssRule {

    /**
     *  文件命名规则
     * @param originalFilename
     * @return
     */
    String setName (String originalFilename);
}
