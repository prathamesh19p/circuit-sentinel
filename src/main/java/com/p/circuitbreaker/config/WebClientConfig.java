package com.p.circuitbreaker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Value("${travel.service.url}")
	private String travelServiceUrl;

	@Bean
	WebClient webClient() {
		return WebClient.builder().baseUrl(travelServiceUrl).build();
	}
}
