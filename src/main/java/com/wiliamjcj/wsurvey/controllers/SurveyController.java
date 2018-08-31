package com.wiliamjcj.wsurvey.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wiliamjcj.wsurvey.dto.SurveyDto;
import com.wiliamjcj.wsurvey.exception.ExceptionLogger;
import com.wiliamjcj.wsurvey.services.SurveyService;
import com.wiliamjcj.wsurvey.utils.APIResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "${controller.survey.mapping}")
public class SurveyController {

	@Value("${controller.survey.mapping}")
	private String BASE_ENDPOINT;

	@Autowired
	ExceptionLogger elog;

	@Autowired
	SurveyService surveyService;

	@ApiOperation(value = "Add a new survey, returning: a generated token, the location and the newly added survey. ")
	@PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<APIResponse<SurveyDto>> addSurvey(@Valid @RequestBody SurveyDto survey, BindingResult res) {
		try {
			APIResponse<SurveyDto> apiResponse = new APIResponse<SurveyDto>();

			if (res.hasErrors()) {
				res.getAllErrors().stream().forEach(err -> apiResponse.getErrors().add(err.getDefaultMessage()));
				return ResponseEntity.badRequest().body(apiResponse);
			}

			survey = surveyService.createSurvey(survey);

			URI location = new URI(BASE_ENDPOINT + "/" + survey.getId());
			apiResponse.setData(survey);
			return ResponseEntity.created(location).header("token", survey.getToken()).body(apiResponse);
		} catch (Exception e) {
			elog.error(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
