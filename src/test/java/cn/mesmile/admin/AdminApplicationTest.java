package cn.mesmile.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zb
 * @Description
 *     注意测试类package，应该和启动类package包一致或者在其子目录下
 */
@SpringBootTest
public class AdminApplicationTest {

    @Test
    public void test () {
        System.out.println("------hello world-----");
    }

}
