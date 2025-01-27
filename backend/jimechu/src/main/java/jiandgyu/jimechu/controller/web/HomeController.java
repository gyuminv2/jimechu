package jiandgyu.jimechu.controller.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String redirectToSwagger() {
        log.info("Redirecting to Swagger UI");
        return "redirect:/swagger-ui/index.html";
    }
}
