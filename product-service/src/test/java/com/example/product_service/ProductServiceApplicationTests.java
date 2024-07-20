package com.example.product_service;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

import io.restassured.RestAssured;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer =new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort//injects the random port to variable (here port)
	private Integer port;

	@BeforeEach//does before each tests
	void setup(){
		RestAssured.baseURI ="http://localhost";
		RestAssured.port=port;
	}

	static{
		mongoDBContainer.start();
	}


	@Test
	void shouldCreateProduct() {
		String requestBody="""
				{
					"name": "Nothing phone 1",
					"description": "yo its a phone ",
					"price": 30000
				}
				""";

		RestAssured.given()
		.contentType("application/json")
		.body(requestBody)
		.when()
		.post("/api/product/")
		.then()
		.statusCode(201)
		.body("id",  Matchers.notNullValue())
		.body("name", Matchers.equalTo("Nothing phone 1"))
		.body("description", Matchers.equalTo("yo its a phone "))
		.body("price", Matchers.equalTo(30000));
	}


}
