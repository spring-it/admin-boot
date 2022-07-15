package cn.mesmile.admin.modules.system.controller;

import cn.mesmile.admin.common.limit.LimiterModeEnum;
import cn.mesmile.admin.common.limit.RateLimiter;
import cn.mesmile.admin.common.lock.RedisLock;
import cn.mesmile.admin.common.oss.OssBuilder;
import cn.mesmile.admin.common.oss.domain.AdminFile;
import cn.mesmile.admin.common.oss.template.OssTemplate;
import cn.mesmile.admin.common.repeat.RepeatSubmit;
import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.utils.AdminRedisTemplate;
import cn.mesmile.admin.modules.system.entity.Sys;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @RateLimiter(max = 3,limiterMode = LimiterModeEnum.LIMITER_ALL)
    @RepeatSubmit(interval = 20000, param = "#word")
    @ApiOperation(value = "用户登录测试接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="word",value="关键字备注",required = true,type="Integer")
    })
    @GetMapping("/get")
    public R hello (@RequestParam("word") String word) {
        return R.data(word);
    }


    @RedisLock(value = "buy",param = "#request.getLocalPort()")
    @RepeatSubmit(interval = 20000)
    @GetMapping("/buy")
    public R buy (HttpServletRequest request){
        int localPort = request.getLocalPort();
        log.info(">>>>>>>>>>>>>>  我的端口号是："+ localPort);
        Integer i = adminRedisTemplate.get("stock");
        if (i != null && i > 0) {
            Long lastNumber = adminRedisTemplate.decr("stock");
            log.info(">>>>>>>>>>>>>>> 扣除成功，剩余库存："+ lastNumber);
        } else {
            log.info(">>>>>>>>>>>>>>> 扣除失败库存失败");
        }
        return R.data("success "+ localPort);
    }

    @RateLimiter(param = "#s.hello")
    @RepeatSubmit(interval = 20000, param = "#s.hello;#s.world")
    @PostMapping("/test")
    public R submit(@RequestBody Sys s){
        System.out.println("s = " + s);
        return R.data(s);
    }

    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file){
        AdminFile adminFile = OssBuilder.build().putFile(file);
        return R.data(adminFile);
    }
}
