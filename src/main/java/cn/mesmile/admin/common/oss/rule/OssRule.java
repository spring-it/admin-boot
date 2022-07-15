package cn.mesmile.admin.common.oss.rule;

/**
 * @author zb
 * @Description
 */
public interface OssRule {

    /**
     *  自定义文件命名规则
     * @param originalFilename 文件源名称
     * @return 自定义路径名称
     */
    String setName (String originalFilename);
}
