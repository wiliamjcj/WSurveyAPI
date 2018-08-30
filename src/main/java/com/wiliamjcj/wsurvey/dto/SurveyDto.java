package com.wiliamjcj.wsurvey.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wiliamjcj.wsurvey.utils.Jsonable;

public class SurveyDto implements Jsonable {

	private Long id;
	
	@JsonIgnore
	private String token;
	
	@NotEmpty(message="{surveydto.question.notnull.msg}")
	private String question;
	
	@Size(min=2, message="{surveydto.options.size.msg}")
	private List<OptionDto> options;
	
	private boolean started;

	private boolean ended;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<OptionDto> getOptions() {
		return options;
	}

	public void setOptions(List<OptionDto> options) {
		this.options = options;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isEnded() {
		return ended;
	}

	public void setEnded(boolean ended) {
		this.ended = ended;
	}
}
