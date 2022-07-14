package cn.mesmile.admin.common.oss;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileUtil;

import java.util.Date;

/**
 * @author zb
 * @Description
 */
public class OssRuleImpl implements OssRule {

    @Override
    public String setName(String originalFilename) {
        Date date = new Date();
        return "upload/" + DatePattern.SIMPLE_MONTH_FORMAT.format(date) + "/" +DatePattern.PURE_DATE_FORMAT.format(date) + "/"
                + originalFilename + "." + FileUtil.extName(originalFilename);
    }

}
