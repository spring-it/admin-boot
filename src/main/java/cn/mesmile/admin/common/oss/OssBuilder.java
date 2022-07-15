package cn.mesmile.admin.common.oss;

import cn.mesmile.admin.common.exceptions.OssException;
import cn.mesmile.admin.common.exceptions.ServiceException;
import cn.mesmile.admin.common.oss.enums.OssTypeEnum;
import cn.mesmile.admin.common.oss.template.MinioTemplate;
import cn.mesmile.admin.common.oss.template.OssTemplate;
import cn.mesmile.admin.common.result.ResultCode;
import cn.mesmile.admin.common.utils.SpringUtil;

/**
 * @author zb
 * @Description Oss客户端操作对象统一调度
 */
public class OssBuilder {

    private OssBuilder(){}

    /**
     *  获取 Oss 统一调度对象
     * @return Oss客户端操作对象
     */
    public static OssTemplate build () {
        OssProperties ossProperties = SpringUtil.getBean(OssProperties.class);
        Boolean enabled = ossProperties.getEnabled();
        if (enabled == null || !enabled){
            throw new OssException(ResultCode.FAILURE, "Oss存储系统未开启");
        }
        OssTypeEnum type = ossProperties.getType();
        if (OssTypeEnum.MINIO_OSS.equals(type)){
            return SpringUtil.getBean(MinioTemplate.class);
        }else if (OssTypeEnum.ALI_OSS.equals(type)){
            // todo 扩展其他oss

        }else if (OssTypeEnum.QIANNIU_OSS.equals(type)) {

        }else if (OssTypeEnum.TENCENT_OSS.equals(type)){

        }
        throw new OssException(ResultCode.FAILURE, "构建oss客户端异常");
    }


}
