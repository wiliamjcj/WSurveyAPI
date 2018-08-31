package com.wiliamjcj.wsurvey.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiliamjcj.wsurvey.dto.OptionDto;
import com.wiliamjcj.wsurvey.dto.SurveyDto;
import com.wiliamjcj.wsurvey.exception.ExceptionLogger;
import com.wiliamjcj.wsurvey.services.SurveyService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = SurveyController.class, secure = false)
public class SurveyControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Value("${controller.survey.mapping}")
	private String BASE_ENDPOINT;
	
	@MockBean
	SurveyService surveyService;
	
	@MockBean
	ExceptionLogger elog;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private static SurveyDto survey1;
	
	@BeforeClass
	public static void setup() {
		survey1 = new SurveyDto();
		survey1.setId(1l);
		survey1.setQuestion("What is your favorite color?");
		survey1.setToken("AS65D4A65S4D654ASF6G5H4FG");
		
		OptionDto option1 = new OptionDto();
		option1.setDescription("Blue");
		option1.setId(1l);
		
		OptionDto option2 = new OptionDto();
		option2.setDescription("Red");
		option2.setId(2l);
		
		survey1.getOptions().add(option1);
		survey1.getOptions().add(option2);
	}
	
	@Test 
	public void addSurveyTest() throws Exception {
		String jsonToSend = "{ "
				+ "		\"question\": \"What is your favorite color?\", "
				+"		\"options\": [ { \"description\": \"Blue\" }, { \"description\": \"Red\" } ] "
		        + "}";
		
		Mockito.when(surveyService.createSurvey(Mockito.any())).thenReturn(survey1);
		
		String expectedResult = mapper.writeValueAsString(survey1);
		
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
	
	@Test 
	public void addSurveyValidationTest() throws Exception {
		//missing the required question property
		String jsonToSend = "{ "
				+"		\"options\": [ { \"description\": \"Blue\" }, { \"description\": \"Red\" } ] "
		        + "}";
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON_UTF8);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		int status = result.getResponse().getStatus();
		assertEquals(HttpStatus.BAD_REQUEST.value(), status);

		JSONObject jo = new JSONObject(result.getResponse().getContentAsString());
		JSONArray errorsArray = jo.getJSONArray("errors");
		
		assertEquals("Survey must have a question.", errorsArray.get(0));
	}
	
	@Test
	public void addSurveyExceptionTest() throws Exception {
		String jsonToSend = "{ "
				+ "		\"question\": \"What is your favorite color?\", "
				+"		\"options\": [ { \"description\": \"Blue\" }, { \"description\": \"Red\" } ] "
		        + "}";
		
		Mockito.when(surveyService.createSurvey(Mockito.any())).thenThrow(new NullPointerException());
		Mockito.doNothing().when(elog).error(Mockito.any());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON_UTF8).content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON_UTF8);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		int status = result.getResponse().getStatus();
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), status);
	}

}
