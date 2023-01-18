package ir.proprog.enrollassist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Locale;

@SpringBootApplication
public class EnrollAssistApplication {

    public static void main(String[] args) {
        // Software is not working on systems with local any other than english
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(EnrollAssistApplication.class, args);
    }

}
