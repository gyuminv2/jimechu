package jiandgyu.jimechu.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*") // 컨트롤러에서 설정
public class helloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello ~! ~! ~! ~!";
    }
}
