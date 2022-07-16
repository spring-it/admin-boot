package cn.mesmile.admin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.HeaderResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author zb
 * @Description web测试样例
 */
@Slf4j
//开启虚拟MVC调用
@AutoConfigureMockMvc
// 在springboot的测试类中通过添加注解@Transactional来阻止测试用例提交事务
//通过注解@Rollback控制springboot测试类执行结果是否提交事务，需要配合注解@Transactional使用
@Transactional(rollbackFor = Exception.class)
@Rollback(value = true)
@SpringBootTest(
        properties = {"test.prop=test20"},
        args = {"--test.prop=test30"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class WebTest {

    /**
     * MOCK：根据当前设置确认是否启动web环境，例如使用了Servlet的API就启动web环境，属于适配性的配置
     * DEFINED_PORT：使用自定义的端口作为web服务器端口
     * RANDOM_PORT：使用随机端口作为web服务器端口
     * NONE：不启动web环境
     *
     *  建议 RANDOM_PORT 模式，避免打包测试出现端口号冲突
     *
     *  properties 自定义测试属性
     *  args  自定义测试传入的参数
     */
    @Value("${test.prop}")
    private String prop;

    @Test
    void test01(@Autowired MockMvc mvc) throws Exception {
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("", "");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addAll(map);
        //创建虚拟请求，当前访问/buy
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/v1/hello/buy")
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        //执行对应的请求
        ResultActions resultActions = mvc.perform(requestBuilder);

        //设定预期值，与真实值进行比较，成功测试通过，失败测试失败
        //定义本次调用的预期值
        StatusResultMatchers status = MockMvcResultMatchers.status();
        //预计本次调用时成功：状态200
        ResultMatcher ok = status.isOk();
        //添加预计值到本次调用过程中进行匹配
        resultActions.andExpect(ok);

        // 非 json 响应体匹配
//        ContentResultMatchers content = MockMvcResultMatchers.content();
//        ResultMatcher result = content.string("Hello~~");
//        resultActions.andExpect(result);

        // json 响应体匹配
        ContentResultMatchers content = MockMvcResultMatchers.content();
        ResultMatcher result =
                content.json("{\"code\":200,\"success\":true,\"data\":\"success 80\",\"msg\":\"操作成功\"}");
        resultActions.andExpect(result);

        // 响应头匹配
        HeaderResultMatchers header = MockMvcResultMatchers.header();
        ResultMatcher contentType =
                header.string("Content-Type", "application/json");
        resultActions.andExpect(contentType);

//        MvcResult mvcResult = resultActions.andReturn();
//        MockHttpServletResponse response = mvcResult.getResponse();
//        String contentAsString = response.getContentAsString(StandardCharsets.UTF_8);
//        System.out.println("contentAsString = " + contentAsString);
    }

    /**
     *  spring自带的随机数产生数据
     *  test-value:
     *   book:
     *     id: ${random.int}
     *     id2: ${random.int(10)}
     *     type: ${random.int!5,10!}
     *     name: ${random.value}
     *     uuid: ${random.uuid}
     *     publishTime: ${random.long}
     */

}
