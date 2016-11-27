package gotogoal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import javax.persistence.EntityManagerFactory;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableEntityLinks;

@SpringBootApplication(exclude={HibernateJpaAutoConfiguration.class,EntityManagerFactory.class})
@EnableJpaRepositories
@EnableEntityLinks
public class GoToGoal3Application {

	public static void main(String[] args) {
		SpringApplication.run(GoToGoal3Application.class, args);
	}
}
