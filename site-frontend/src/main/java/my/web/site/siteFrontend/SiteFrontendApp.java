package my.web.site.siteFrontend;

import my.web.site.commons.SiteCommonsApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackageClasses = {SiteFrontendApp.class, SiteCommonsApp.class})
public class SiteFrontendApp {

    public static void main(String... args){
        SpringApplication.run(SiteFrontendApp.class);
    }

}
