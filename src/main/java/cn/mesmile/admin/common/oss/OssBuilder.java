package cn.mesmile.admin.common.oss;

import cn.mesmile.admin.common.exceptions.ServiceException;
import cn.mesmile.admin.common.result.ResultCode;
import cn.mesmile.admin.common.utils.SpringUtil;

/**
 * @author zb
 * @Description
 */
public class OssBuilder {

    public static OssTemplate build(){
        OssProperties ossProperties = SpringUtil.getBean(OssProperties.class);
        OssTypeEnum type = ossProperties.getType();

        if (OssTypeEnum.MINIO_OSS.equals(type)){
            return SpringUtil.getBean(MinioTemplate.class);
        }else if (OssTypeEnum.ALI_OSS.equals(type)){

        }else if (OssTypeEnum.QIANNIU_OSS.equals(type)) {

        }else if (OssTypeEnum.TENCENT_OSS.equals(type)){

        }else {
            throw new ServiceException(ResultCode.FAILURE, "构建oss客户端异常");
        }
        return null;
    }


}
