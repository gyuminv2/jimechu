package jiandgyu.jimechu.config.init;


import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDataLoader implements CommandLineRunner {

    private final InitDataService initDataService;

    @Override
    public void run(String... args) {
        initDataService.initData();
    }
}