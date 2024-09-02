package com.example.demo.Model;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuestionWithOptions {
	private String question;
    private List<String> options;

    public QuestionWithOptions(String question, List<String> options) {
        this.question = question;
        this.options = options;
    }

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}
   
}
