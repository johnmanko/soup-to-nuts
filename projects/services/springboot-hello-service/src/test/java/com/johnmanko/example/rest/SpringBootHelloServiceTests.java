package com.johnmanko.example.rest;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
@DisplayName("Testing Hello API")
class SpringBootHelloServiceTests {

	@Autowired
	private MockMvc app;

	@Test
	@Order(1)
	@DisplayName("Endpoint: GET / (Hello World)")
	void sayHello() throws Exception {
		app.perform(get("/api/v1/hello").accept(MediaType.TEXT_PLAIN))
				.andExpect(status().isOk())
				.andExpect(content().string("Hello World!"))
				.andDo(document("hello-world"));;
	}

	@Test
	@Order(2)
	@DisplayName("Endpoint: GET /greeting (greeting.template and GREETING_SUBJECT)")
	void greeting() throws Exception {
		app.perform(get("/api/v1/hello/greeting").accept(MediaType.TEXT_PLAIN))
				.andExpect(status().isOk())
				.andExpect(content().string("What's up, Doc"))
				.andDo(document("greeting"));
	}


	@Test
	@Order(3)
	@DisplayName("Endpoint: GET /greeting/{subject} (greeting.template and {subject})")
	void greetingTo() throws Exception {
		app.perform(get("/api/v1/hello/greeting/Moe").accept(MediaType.TEXT_PLAIN))
				.andExpect(status().isOk())
				.andExpect(content().string("What's up, Moe"))
				.andDo(document("greeting-subject"));
	}
}
