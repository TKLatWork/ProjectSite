package my.site.account.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.auth")
@Data
public class AuthConfig {

    private String secret;//JWT的密匙
    private Long expirationTime;//过期时间
    private final String adminAccountName = "admin";
    private String adminPassword;

}
