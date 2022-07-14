package cn.mesmile.admin.common.oss;

/**
 * @author zb
 * @Description
 */
public class OssBuilder {

    public static OssTemplate build(){
        return new MinioTemplate();
    }


}
