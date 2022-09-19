package br.com.jadson.gaugeci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 *
 * Execute the application by command line to use REST Api
 *
 * java -jar -Dserver.port=8080 gauge-ci.jar
 */
@EnableWebMvc
@SpringBootApplication
public class GaugeCiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaugeCiApplication.class, args);
	}

}
