package link.s.sls.init;
import link.s.sls.config.SystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Component
@Slf4j
public class Init implements CommandLineRunner {

    @Autowired
    private SystemProperties systemProperties;

    @Override
    public void run(String... args) throws Exception {


    }
}
