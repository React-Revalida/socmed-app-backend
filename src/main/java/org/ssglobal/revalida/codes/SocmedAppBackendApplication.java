package org.ssglobal.revalida.codes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.ssglobal.revalida.codes.config.RsaKeyProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class SocmedAppBackendApplication {

    public static void main(final String[] args) {
        SpringApplication.run(SocmedAppBackendApplication.class, args);
    }

}
