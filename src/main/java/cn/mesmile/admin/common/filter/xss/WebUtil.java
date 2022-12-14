package cn.mesmile.admin.common.filter.xss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author zb
 * @Description
 */
@Slf4j
public class WebUtil {

    /**
     * 获取body中的数据
     *
     * @param servletInputStream 输入流
     * @return body字符串
     */
    public static String getRequestBody(ServletInputStream servletInputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(servletInputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            log.error("io读数据异常", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    log.error("reader关闭流异常", e);
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (Exception e) {
                    log.error("inputStreamReader关闭流异常", e);
                }
            }
            if (servletInputStream != null) {
                try {
                    servletInputStream.close();
                } catch (Exception e) {
                    log.error("servletInputStream关闭流异常", e);
                }
            }
        }
        return sb.toString();
    }

    public static void renderString(HttpServletResponse response,String text, int status) {
        try {
            Assert.isTrue(status > 0, "状态码有误");
            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(text);
        } catch (IOException e) {
            log.error("返回结果异常", e);
        }
    }

    public static void renderStringSuccess(HttpServletResponse response, String text) {
        renderString(response, text, 200);
    }
}
