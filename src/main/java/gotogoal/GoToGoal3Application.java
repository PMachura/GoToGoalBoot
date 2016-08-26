package gotogoal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import gotogoal.config.PictureUploadProperties;

@SpringBootApplication(exclude={HibernateJpaAutoConfiguration.class})
@EnableConfigurationProperties({PictureUploadProperties.class})
public class GoToGoal3Application {

	public static void main(String[] args) {
		SpringApplication.run(GoToGoal3Application.class, args);
	}
}
