package SemanticWeb.SNCF;

import SemanticWeb.SNCF.Utility.TextfileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SncfApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SncfApplication.class, args);
	}
}

