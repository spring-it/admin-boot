package cn.mesmile.admin.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.Optional;

/**
 * @author zb
 * @Description 初始化提示
 */
@Slf4j
@Component
@Order(value = 1)
public class CommandLineRunnerHandlerImpl implements CommandLineRunner {

    @Resource
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        InetAddress localHost = InetAddress.getLocalHost();
        if (localHost == null){
            return;
        }
        Optional<String> hostOptional = Optional.ofNullable(localHost.getHostAddress());
        String ip = hostOptional.orElseGet(() -> "localhost");
        Optional<String> portOptional = Optional.ofNullable(environment.getProperty("server.port"));
        String port = portOptional.orElseGet(() -> "未读取到端口");
        Optional<String> activeOptional = Optional.ofNullable(environment.getProperty("spring.profiles.active"));
        String active = activeOptional.orElseGet(() -> "未读取到分支");
        log.info("\n\n\tadmin   当前分支: \t" + active + "\n" +
                "\tswagger 文档地址: \thttp://" + ip + ":" + port + "/doc.html");
    }

}