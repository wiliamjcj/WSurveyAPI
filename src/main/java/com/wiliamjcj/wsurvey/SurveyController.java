package com.wiliamjcj.wsurvey;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wiliamjcj.wsurvey.utils.APIResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "${controller.survey.mapping}")
public class SurveyController {

	@Value("${controller.survey.mapping}")
	private String BASE_ENDPOINT;

	@ApiOperation(value = "Add a new survey, returning: a generated token, the location and the newly added survey. ")
	@PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<APIResponse<String>> addSurvey(@Valid @RequestBody String enquete, BindingResult res) throws URISyntaxException {

		APIResponse<String> apiResponse = new APIResponse<String>();

		if (res.hasErrors()) {
			res.getAllErrors().stream().forEach(err -> apiResponse.getErrors().add(err.getDefaultMessage()));
			return ResponseEntity.badRequest().body(apiResponse);
		}

		URI location = new URI(BASE_ENDPOINT);
		apiResponse.setData("{\"data\":\"ok\"}");
		return ResponseEntity.created(location).header("token", "").body(apiResponse);
	}
}
