package cn.mesmile.admin.modules.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zb
 * @Description
 */
@Slf4j
@RequestMapping("/api/v1/hello")
@RestController
public class HelloController {

    @GetMapping("/get")
    public String hello (@RequestParam("word") String word) {
        System.out.println(word);
        return word;
    }

}
