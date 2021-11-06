package link.s.sls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(value = "link.s")
public class SLSApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SLSApplication.class);
        application.addListeners(new ApplicationPidFileWriter("./sls.pid"));
        application.run(args);
    }


}
