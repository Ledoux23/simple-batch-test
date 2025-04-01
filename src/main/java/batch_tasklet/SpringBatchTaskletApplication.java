package batch_tasklet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling	//Si tu utilises @Scheduled(fixedRate = )
public class SpringBatchTaskletApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchTaskletApplication.class, args);
	}

}
