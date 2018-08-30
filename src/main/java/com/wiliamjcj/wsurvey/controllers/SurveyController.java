package com.wiliamjcj.wsurvey.controllers;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wiliamjcj.wsurvey.dto.SurveyDto;
import com.wiliamjcj.wsurvey.utils.APIResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "${controller.survey.mapping}")
public class SurveyController {

	@Value("${controller.survey.mapping}")
	private String BASE_ENDPOINT;

	@ApiOperation(value = "Add a new survey, returning: a generated token, the location and the newly added survey. ")
	@PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<APIResponse<SurveyDto>> addSurvey(@Valid @RequestBody SurveyDto survey, BindingResult res) throws URISyntaxException {

		APIResponse<SurveyDto> apiResponse = new APIResponse<SurveyDto>();

		if (res.hasErrors()) {
			res.getAllErrors().stream().forEach(err -> apiResponse.getErrors().add(err.getDefaultMessage()));
			return ResponseEntity.badRequest().body(apiResponse);
		}
		
		survey.setId(1l);
		survey.getOptions().get(0).setId(1l);
		survey.getOptions().get(1).setId(2l);

		URI location = new URI(BASE_ENDPOINT);
		apiResponse.setData(survey);
		return ResponseEntity.created(location).header("token", "").body(apiResponse);
	}
}
