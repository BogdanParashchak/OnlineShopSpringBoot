package com.parashchak.online.shop.spring.boot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OnlineShopAcceptanceTest {

	@Value("${local.server.port}")
	int randomServerPort;

	@Test
	void whenRequestForAllProducts_thenOkStatus() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:" + randomServerPort + "/products";
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
		assertEquals(OK, responseEntity.getStatusCode());
	}
}