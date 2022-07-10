package cn.mesmile.admin.modules.system.controller;

import cn.mesmile.admin.common.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.net.www.protocol.http.HttpURLConnection;

/**
 * @author zb
 * @Description
 */
@Api(tags = "用户登录测试接口")
@Slf4j
@RequestMapping("/api/v1/hello")
@RestController
public class HelloController {

    @ApiOperation(value = "用户登录测试接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="word",value="关键字备注",required = true,type="Integer")
    })
    @GetMapping("/get")
    public R hello (@RequestParam("word") String word) {
        return R.data(word);
    }

}
