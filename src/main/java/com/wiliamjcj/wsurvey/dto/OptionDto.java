package com.wiliamjcj.wsurvey.dto;

import javax.validation.constraints.NotEmpty;

import com.wiliamjcj.wsurvey.utils.Jsonable;

public class OptionDto implements Jsonable
{

    private Long id;
    private long votes;

    @NotEmpty(message = "{optiondto.description.notnull.msg}")
    private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getVotes() {
		return votes;
	}

	public void setVotes(long votes) {
		this.votes = votes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
