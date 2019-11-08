package mitreCrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonsterCrawlerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MonsterCrawlerApplication.class, args);
	}
}
