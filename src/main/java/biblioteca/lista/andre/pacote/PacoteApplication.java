package biblioteca.lista.andre.pacote;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PacoteApplication {
    
    
    @PostConstruct
      void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-3:00"));
      }



	public static void main(String[] args) {
		SpringApplication.run(PacoteApplication.class, args);
	}
}
