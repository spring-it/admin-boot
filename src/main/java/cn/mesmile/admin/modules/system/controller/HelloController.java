package cn.mesmile.admin.modules.system.controller;

import cn.hutool.core.date.DateUtil;
import cn.mesmile.admin.common.excel.EasyExcelUtil;
import cn.mesmile.admin.common.limit.LimiterModeEnum;
import cn.mesmile.admin.common.limit.RateLimiter;
import cn.mesmile.admin.common.lock.RedisLock;
import cn.mesmile.admin.common.oss.OssBuilder;
import cn.mesmile.admin.common.oss.domain.AdminFile;
import cn.mesmile.admin.common.rabbit.constant.RabbitConstant;
import cn.mesmile.admin.common.repeat.RepeatSubmit;
import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.utils.AdminRedisTemplate;
import cn.mesmile.admin.common.utils.ResourceI18nUtil;
import cn.mesmile.admin.modules.message.operational.ISendService;
import cn.mesmile.admin.modules.message.vo.MqMessageVO;
import cn.mesmile.admin.modules.system.entity.Sys;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ISendService sendService;

    /**
     *  客户端工具：https://mqttx.app/
     */
    @GetMapping("/message")
    public R message(){
        MqMessageVO mqMessageVO = new MqMessageVO();
        mqMessageVO.setReceiveUser("123");
        mqMessageVO.setContent("设置内容11111111111");
        sendService.sendSingleMessage("123",mqMessageVO);
        return R.data("test");
    }

    @GetMapping("/direct")
    public R send(){
        rabbitTemplate.convertAndSend(RabbitConstant.DIRECT_MODE_EXCHANGE_ONE,
                RabbitConstant.DIRECT_MODE_QUEUE_ONE,"direct message");
        return R.data("发送成功direct");
    }

    @GetMapping("/fanout")
    public R fanout(){
        rabbitTemplate.convertAndSend(RabbitConstant.FANOUT_MODE_EXCHANGE_ONE,
                "","fanout message");
        return R.data("发送成功fanout");
    }

    @GetMapping("/topic")
    public R topic(){
        rabbitTemplate.convertAndSend(RabbitConstant.TOPIC_MODE_EXCHANGE_ONE,
                "topic.mode.test.multi","topic message");
        return R.data("发送成功topic");
    }

    @GetMapping("/deadLetter")
    public R deadLetter(){
        rabbitTemplate.convertAndSend(RabbitConstant.TOPIC_MODE_EXCHANGE_TWO,
                "topic.dead.letter.test","deadLetter message "+ DateUtil.now());
        return R.data("发送成功topic");
    }

    @GetMapping("/delay")
    public R delay(){
        rabbitTemplate.convertAndSend(RabbitConstant.DELAY_MODE_EXCHANGE,
                RabbitConstant.DELAY_MODE_QUEUE, "delay message, delay 8s, " + DateUtil.now(),
                    message -> {
                        message.getMessageProperties().setHeader("x-delay", 8000);
                        return message;
                    }
                );
        return R.data("发送成功delay");
    }


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

    /**
     * localhost:8080/api/v1/hello/down?fileName=/upload/202208/20220806/am.pak
     */
    @Deprecated
    @ApiIgnore
    @GetMapping("/down")
    public ResponseEntity<byte[]> down(@RequestParam("fileName") String fileName){
        ResponseEntity<byte[]> download = OssBuilder.build().download(fileName);
        return download;
    }

    /**
     * localhost:8080/api/v1/hello/preview?fileName=/upload/202208/20220806/am.pak
     */
    @Deprecated
    @ApiIgnore
    @GetMapping("/preview")
    public R preview(@RequestParam("fileName") String fileName){
        String preview = OssBuilder.build().preview(fileName);
        return R.data(preview);
    }


    @GetMapping("/i18")
    public R get(){
        // 参数优先 admin_language   默认根据请求头判断 Accept-Language
        String username = ResourceI18nUtil.getValueByKey("user.login.username");
        return R.data(username);
    }

    @PostMapping("/excel")
    public R excel(@RequestParam("file") MultipartFile file){
        EasyExcelUtil.readAndSave(file,System.out::println,Sys.class);
        return R.data("success");
    }

    @PostMapping("/read")
    public R read(@RequestParam("file") MultipartFile file){
        List<Sys> read = EasyExcelUtil.read(file, Sys.class);
        return R.data(read);
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response){
        ArrayList<Sys> sysList = new ArrayList<>();
        sysList.add(new Sys("数据①","word数据1"));
        sysList.add(new Sys("数据②","word数据2"));
        EasyExcelUtil.export(response,"导出测试", sysList, Sys.class);
    }

    @GetMapping("/export2")
    public void export2(HttpServletResponse response) throws IOException {
        ArrayList<Sys> sysList = new ArrayList<>();
        sysList.add(new Sys("数据①","word数据1"));
        sysList.add(new Sys("数据②","word数据2"));
        EasyExcelUtil.exportAndWatermark(response,"导出测试", sysList, Sys.class,"我是水印IAmWatermark");
    }

}
