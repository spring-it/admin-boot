package cn.mesmile.admin.common.filter.space;

import cn.hutool.core.util.StrUtil;
import cn.mesmile.admin.common.filter.xss.StringJsonUtils;
import cn.mesmile.admin.common.filter.xss.WebUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * xss过滤包装器
 * @author zb
 */
@Slf4j
public class SpaceHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final HttpServletRequest orgRequest;
    private byte[] body;

    public SpaceHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.orgRequest = request;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (super.getHeader("Content-Type") == null) {
            return super.getInputStream();
        } else if (super.getHeader("Content-Type").startsWith("multipart/form-data")) {
            return super.getInputStream();
        } else {
            if (this.body == null) {
                String requestBody = WebUtil.getRequestBody(super.getInputStream());
                // 去除两边空格
                if (StrUtil.isNotEmpty(requestBody)){
                    // 去除json字符串中所有类型为string两边的空格
                    Map<String, Object> stringObjectMap = StringJsonUtils.
                            jsonStringToMapAndTrim(requestBody, false, true);
                    requestBody = JSONObject.toJSONString(stringObjectMap);
                }else {
                    // 为空则直接返回
                    return super.getInputStream();
                }
                this.body = requestBody.getBytes();
            }
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.body);
            return new ServletInputStream() {
                @Override
                public int read() {
                    return byteArrayInputStream.read();
                }
                @Override
                public boolean isFinished() {
                    return false;
                }
                @Override
                public boolean isReady() {
                    return false;
                }
                @Override
                public void setReadListener(ReadListener readListener) {
                }
            };
        }
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(this.spaceTrim(name));
        if (StrUtil.isNotBlank(value)) {
            value = this.spaceTrim(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters != null && parameters.length != 0) {
            for(int i = 0; i < parameters.length; ++i) {
                parameters[i] = this.spaceTrim(parameters[i]);
            }
            return parameters;
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new LinkedHashMap();
        Map<String, String[]> parameters = super.getParameterMap();
        Iterator iterator = parameters.keySet().iterator();
        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            String[] values = (String[])parameters.get(key);
            for(int i = 0; i < values.length; ++i) {
                values[i] = this.spaceTrim(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(this.spaceTrim(name));
        if (StrUtil.isNotBlank(value)) {
            value = this.spaceTrim(value);
        }
        return value;
    }


    private String spaceTrim(String input) {
        // 去除字符串两边空格
        return StrUtil.trim(input);
    }

    public HttpServletRequest getOrgRequest() {
        return this.orgRequest;
    }

    public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
        return request instanceof SpaceHttpServletRequestWrapper ? ((SpaceHttpServletRequestWrapper)request).getOrgRequest() : request;
    }
}
