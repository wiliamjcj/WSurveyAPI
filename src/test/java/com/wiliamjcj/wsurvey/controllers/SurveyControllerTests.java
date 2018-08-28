package com.wiliamjcj.wsurvey.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.wiliamjcj.wsurvey.SurveyController;

@RunWith(SpringRunner.class)
@WebMvcTest(value = SurveyController.class, secure = false)
public class SurveyControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Value("${controller.survey.mapping}")
	private String BASE_ENDPOINT;
	
	@Test 
	public void addSurveyTest() throws Exception {
		String jsonToSend = "{}";
		String expectedResult = "{\"data\":\"ok\"}";
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON_UTF8);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		int status = result.getResponse().getStatus();
		assertEquals(HttpStatus.CREATED.value(), status);

		String location = result.getResponse().getHeader("location");
		assertThat(location).isNotBlank();

		JSONObject jo = new JSONObject(result.getResponse().getContentAsString());
		Object returnedResult = jo.get("data");
		
		JSONAssert.assertEquals(expectedResult, returnedResult.toString(), false);
	}
	

}
