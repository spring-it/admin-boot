package cn.mesmile.admin.modules.scheduled;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author zb
 * @Description
 */
@Slf4j
@ConditionalOnProperty(value = {"xxl.job.enabled"}, havingValue = "true")
@EnableConfigurationProperties({XxlJobProperties.class})
@Configuration
public class XxlJobConfiguration {

    @Resource
    private XxlJobProperties xxlJobProperties;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getServiceAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppName());
        xxlJobSpringExecutor.setAddress(xxlJobProperties.getClientAddress());
        xxlJobSpringExecutor.setIp(xxlJobProperties.getClientIp());
        xxlJobSpringExecutor.setPort(xxlJobProperties.getClientPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getClientLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getClientLogRetentionDays());
        return xxlJobSpringExecutor;
    }

    /*
     *                  默认用户名：admin
     *                  默认密码：123456
     *                  服务端版本：2.3.1      http://81.69.43.78:8090/xxl-job-admin/
     *                  文档以及学习： https://www.xuxueli.com/xxl-job
     *
     *                  钉钉外网映射：
     *                  git clone https://github.com/open-dingtalk/pierced.git
     *                  打开项目的目录  cd windows_64
     *                  ding -config=ding.cfg -subdomain=mesmile 9999
     *                  映射结果  http://mesmile.vaiwan.com -> 127.0.0.1:9999
     */

}
