package com.game.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

public class Questions {
	public class QuestionCard {
		private String question;
		private String response;
		
		public QuestionCard(String q, String r) {
			question = q;
			response = r;
		}

		public String getQuestion() {
			return question;
		}
		
		public String getResponse() {
			return response;
		}
	}

	private ArrayList<String> categories = new ArrayList<String>();
	private Map<String, ArrayList<QuestionCard>> questionsByCategories = new HashMap<>();
	
	public Questions() throws IOException {
		URL url = new URL("https://gitlab.com/Kannitsh/alexa/raw/master/questions.json");
		InputStream in = url.openStream();

		StringBuilder jsonString = new StringBuilder();
		BufferedReader file = new BufferedReader(new InputStreamReader(in, Charset.forName("utf8")));
		file.lines().forEachOrdered(e -> jsonString.append(e));

		JSONObject json = new JSONObject(jsonString.toString());
		Stream.of(JSONObject.getNames(json)).forEach(e -> categories.add(e));
		
		for (String categorie : categories) {
			JSONArray categorieQuestion = json.getJSONArray(categorie);
			ArrayList<QuestionCard> questionCards = new ArrayList<>();

			for (int i = 0; i <  categorieQuestion.length(); i++) {
				JSONObject jo = categorieQuestion.getJSONObject(i);
				String q = jo.getString("question");
				String r = jo.getString("reponse");
				questionCards.add(new QuestionCard(q, r));
			}
			
			questionsByCategories.put(categorie, questionCards);
		}
	}
	
	public QuestionCard getQuestion(String category) {
		ArrayList<QuestionCard> questions = questionsByCategories.get(category);
		int index = (int) (Math.random() * questions.size());
		return questions.get(index);
	}

	public void reinsertQuestion(String category, QuestionCard questionCard) {
		questionsByCategories.get(category).add(questionCard);
	}
}
