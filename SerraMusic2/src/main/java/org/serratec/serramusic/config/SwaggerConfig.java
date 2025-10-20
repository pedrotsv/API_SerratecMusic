package org.serratec.serramusic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("Serratec Music API")
				.version("1.0.0")
				.description("API REST para gerenciamento de usuários, artistas, músicas e playlists. ")
				.contact(new Contact()
					.name("Pedro Tavares")
					.email("contato@serratecmusic.com")
					.url(""))
				.license(new License()
					.name("Apache License")
					.url("https://www.apache.org/licenses/LICENSE-2.0")));
	}
}	