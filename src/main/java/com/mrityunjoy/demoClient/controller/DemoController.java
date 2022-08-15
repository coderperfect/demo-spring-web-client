package com.mrityunjoy.demoClient.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {
	@Value("${HOST_URL:http://localhost:8080}")
	private String hostUrl;
	
	@GetMapping("/weak-login")
	public Mono<TokenResponse> loginWeak() {
		String username = "begula";
		String password = "abc";
		
		WebClient webClient = WebClient.builder()
				.filter(ExchangeFilterFunctions
						.basicAuthentication(username, password))
				.build();
		
		return webClient.get().uri(hostUrl + "/login")
				.exchangeToMono(res -> {
			log.info("Sending token weak security");
			return res.bodyToMono(TokenResponse.class);
		});
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	private static class TokenResponse {
		private String token;
		private String username;
		private List<Map<String, String>> authorities;
	}
}
