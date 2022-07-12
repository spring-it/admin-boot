package cn.mesmile.admin.modules.system.controller;

import cn.mesmile.admin.common.lock.RedisLock;
import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.utils.AdminRedisTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zb
 * @Description
 */
@Api(tags = "用户登录测试接口")
@Slf4j
@RequestMapping("/api/v1/hello")
@RestController
public class HelloController {

    @Resource
    private AdminRedisTemplate adminRedisTemplate;

    @ApiOperation(value = "用户登录测试接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="word",value="关键字备注",required = true,type="Integer")
    })
    @GetMapping("/get")
    public R hello (@RequestParam("word") String word) {
        return R.data(word);
    }


    @RedisLock(value = "buy",param = "#request.getLocalPort()")
    @GetMapping("/buy")
    public R buy (HttpServletRequest request){
        int localPort = request.getLocalPort();
        log.info(">>>>>>>>>>>>>>  我的端口号是："+ localPort);
        Integer i = adminRedisTemplate.get("stock");
        if (i > 0) {
            Long lastNumber = adminRedisTemplate.decr("stock");
            log.info(">>>>>>>>>>>>>>> 扣除成功，剩余库存："+ lastNumber);
        } else {
            log.info(">>>>>>>>>>>>>>> 扣除失败库存失败");
        }
        return R.data("success "+ localPort);
    }

}
