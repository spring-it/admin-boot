package cn.mesmile.admin.common.utils;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zb
 * @Description 自定义工具类
 */
@Slf4j
public class FunctionUtil {


    /**
     * 将英文逗号分割的字符串，转换成 List
     * @param string 字符串
     * @return 结果
     */
    public static List<Long> strToLongList(String string) {
        return strToList(string, StringPool.COMMA);
    }

    /**
     * 将某个字符串分割的字符串转换成，List
     * @param string 字符串
     * @param split 分隔符
     * @return 结果
     */
    public static List<Long> strToList(String string,String split){
        if (string == null || split == null){
            return new ArrayList<>();
        }
        String[] resultArray = string.split(split);
        List<Long> result = new ArrayList<>(resultArray.length);
        for (String str : resultArray) {
            if (NumberUtil.isNumber(str)){
                try {
                    long number = Long.parseLong(str);
                    result.add(number);
                }catch (Exception e){
                    log.error("解析数据异常", e);
                }
            }
        }
       return result;
    }
}
