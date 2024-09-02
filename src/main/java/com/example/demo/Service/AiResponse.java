package com.example.demo.Service;

import com.example.demo.Model.QuestionWithOptions;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.util.StringBuilders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AiResponse {

	@Value("${gemini.api.key}")
	private String apiKey;

	@Value("${gemini.api.url}")
	private String apiUrl;

	private final ObjectMapper objectMapper;

	public AiResponse(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public List<String> getResponseFromGemini(String query) throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		String jsonPayload = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", query);

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl + "?key=" + apiKey))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8)).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		JsonNode jsonNode = objectMapper.readTree(response.body());
		String Message = jsonNode.at("/candidates/0/content/parts/0/text").asText();
		// List<QuestionWithOptions> questionsWithOptions =
		// extractQuestionsWithOptions(result);
		// return questionsWithOptions;
		// return extractQuestion(Message);
		return QuestionPrint(Message);
	}

	public List<String> QuestionPrint(String question) {
		// create by Great Java Developer name Pankaj Kanojiya
		System.out.println(question);
		List<String> list = new ArrayList<>();
		StringBuilder st = new StringBuilder();
		for (int i = 0; i < question.length(); i++) {
			if (Character.isDigit(question.charAt(i))) {
				while (question.charAt(i) != '?') {
					if(question.charAt(i) == '*') {i =  i + 2;}
					st.append(question.charAt(i));
					i++;
				}
				list.add(st.toString());
				st.setLength(0);
			}
		}
		//System.out.print(list);
		return list;
	}

	public List<String> extractQuestion(String response) {
		System.out.println(response);
		List<String> question = new ArrayList<>();
		boolean Cheak = false;
		StringBuilder quest = new StringBuilder();
		for (int i = 0; i < response.length() - 1; i++) {
			if ((response.charAt(i) == '*') && (response.charAt(i + 1) == '*')) {
				Cheak = (Cheak) ? false : true;
				if (!Cheak) {
					question.add(quest.toString());
					quest.setLength(0);
				}
				i = i + 2;
			}
			if (Cheak) {
				quest.append(response.charAt(i));
			}
		}
		return question;
	}

	public List<QuestionWithOptions> extractQuestionsWithOptions(String resoponse) {
		List<QuestionWithOptions> questionsWithOptions = new ArrayList<>();

		// Regular expression to match questions with their options
		String questionRegex = "(\\d+\\.\\s.*?\\?)\\s*\\*";
		Pattern questionPattern = Pattern.compile(questionRegex, Pattern.DOTALL);
		Matcher questionMatcher = questionPattern.matcher(resoponse);

		int lastEnd = 0;
		while (questionMatcher.find()) {
			String question = questionMatcher.group(1).trim();
			int start = questionMatcher.start();
			int end = questionMatcher.end();

			// Extract options for the question
			String optionsInput = resoponse.substring(end).trim();
			int nextQuestionStart = findNextQuestionStart(optionsInput);
			if (nextQuestionStart != -1) {
				optionsInput = optionsInput.substring(0, nextQuestionStart).trim();
			}

			List<String> options = extractOptions(optionsInput);

			questionsWithOptions.add(new QuestionWithOptions(question, options));

			lastEnd = end;
		}

		return questionsWithOptions;
	}

	private static int findNextQuestionStart(String input) {
		String nextQuestionRegex = "\\d+\\.\\s";
		Pattern pattern = Pattern.compile(nextQuestionRegex);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.start();
		}
		return -1;
	}

	private static List<String> extractOptions(String optionsInput) {
		List<String> options = new ArrayList<>();
		String optionRegex = "\\*\\s(.*?)(?=\\s*\\d+\\.\\s|$)";
		Pattern pattern = Pattern.compile(optionRegex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(optionsInput);

		while (matcher.find()) {
			options.add(matcher.group(1).trim());
		}

		return options;
	}
}
