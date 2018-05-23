
package org.basaraba.flowers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.run(args);
    }

    public static ApplicationContext mainWithContext(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        return application.run(args);
    }
}
