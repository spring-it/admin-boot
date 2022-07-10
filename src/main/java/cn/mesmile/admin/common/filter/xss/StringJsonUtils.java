package cn.mesmile.admin.common.filter.xss;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.text.StringEscapeUtils;

import java.util.*;

/**
 * @author zb
 * @Description
 */
public class StringJsonUtils {
    /**
     * 去除json字符串中所有类型为string两边的空格
     *
     * @param jsonString 需要处理的json字符串
     * @param escapeHtml 是否转义html符号
     * @param trim       是否去除两个空格
     */
    public static Map<String, Object> jsonStringToMapAndTrim(String jsonString, boolean escapeHtml, boolean trim) {
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        for (Object k : jsonObject.keySet()) {
            Object o = jsonObject.get(k);
            if (o instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<>();
                Iterator<Object> it = ((JSONArray) o).iterator();
                while (it.hasNext()) {
                    Object obj = it.next();
                    list.add(jsonStringToMapAndTrim(obj.toString(), escapeHtml, trim));
                }
                map.put(k.toString(), list);
            } else if (o instanceof JSONObject) {
                // 如果内层是json对象的话，继续解析
                map.put(k.toString(), jsonStringToMapAndTrim(o.toString(), escapeHtml, trim));
            } else {
                // 如果内层是普通对象的话，直接放入map中
                if (o instanceof String) {
                    String s = o.toString();
                    if (escapeHtml) {
                        // 转义成 html 4
                        s = StringEscapeUtils.escapeHtml4(s);
                    }
                    if (trim) {
                        // 去除字符串两边空格
                        s = StrUtil.trim(s);
                    }
                    map.put(k.toString(), s);
                } else {
                    map.put(k.toString(), o);
                }
            }
        }
        return map;
    }
}