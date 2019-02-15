package com.game.model;

import java.util.ArrayList;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GameSession {

	public enum GameStep {
		PLAYER_COUNT, PLAYER_NAME, PLAYER_AGE, WAIT_TO_START, SELECT_CATEGORY, QUESTION, GAMEOVER;

		public GameStep increment() {
			switch (this) {
				case PLAYER_COUNT:
					return PLAYER_NAME;
				case PLAYER_NAME:
					return PLAYER_AGE;
				case PLAYER_AGE:
					return WAIT_TO_START;
				case WAIT_TO_START:
					return SELECT_CATEGORY;
				case SELECT_CATEGORY:
					return QUESTION;
				case QUESTION:
					return GAMEOVER;
				default:
					throw new IllegalStateException();
			}
		}
	}

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private HttpSession session;

	public GameSession(HttpSession session) {
		this.session = session;
	}

	public GameStep getStep() {
		return MAPPER.convertValue(session.getAttribute("step"), GameStep.class);
	}

	public void setStep(GameStep step) {
		session.setAttribute("step", step);
	}

	public void incrementStep() {
		session.setAttribute("step", getStep().increment());
	}

	public boolean isGameOver() {
		return getStep() == GameStep.GAMEOVER;
	}

	public int getPlayerCount() {
		return (int) session.getAttribute("playerCount");
	}

	public void setPlayerCount(int playerCount) {
		session.setAttribute("playerCount", playerCount);
	}

	public int getCurrentPlayerId() {
		return (int) session.getAttribute("currentPlayer");
	}

	public void setCurrentPlayerId(int currentPlayer) {
		session.setAttribute("currentPlayer", currentPlayer);
	}

	public ArrayList<Player> getPlayersInfoArray() {
		Player[] array = MAPPER.convertValue(session.getAttribute("playersInfoArray"), Player[].class);
		ArrayList<Player> arrayList = new ArrayList<>();
		Stream.of(array).forEach(e -> arrayList.add(e));
		return arrayList;
	}

	public void setPlayersInfoArray(ArrayList<Player> playersInfo) {
		session.setAttribute("playersInfoArray", playersInfo.toArray(new Player[0]));
	}

	public String getPlayerName(int playerId) {
		return getPlayersInfoArray().get(playerId).getName();
	}
	
	public void setPlayerName(int playerId, String name) {
		ArrayList<Player> players = getPlayersInfoArray();
		players.get(playerId).setName(name);
		setPlayersInfoArray(players);
	}

	public int getPlayerAge(int playerId) {
		return getPlayersInfoArray().get(playerId).getAge();
	}
	
	public void setPlayerAge(int playerId, int age) {
		ArrayList<Player> players = getPlayersInfoArray();
		players.get(playerId).setAge(age);
		setPlayersInfoArray(players);
	}

	public int getPlayerPoints(int playerId) {
		return getPlayersInfoArray().get(playerId).getPoints();
	}

	public void setPlayerPoints(int playerId, int playerPoint) {
		ArrayList<Player> players = getPlayersInfoArray();
		players.get(playerId).setPoints(playerPoint);
		setPlayersInfoArray(players);
	}

	public void incrementPlayerPoints(int playerId) {
		setPlayerPoints(playerId, getPlayerPoints(playerId) + 1);
	}
	
	public int getPlayerPosition(int playerId) {
		return getPlayersInfoArray().get(playerId).getPosition();
	}
	
	public void setPlayerPosition(int playerId, int position) {
		ArrayList<Player> players = getPlayersInfoArray();
		players.get(playerId).setPosition(position);
		setPlayersInfoArray(players);
	}
	
	public String getCategory() {
		return (String) session.getAttribute("category");
	}
	
	public void setCategory(String category) {
		session.setAttribute("category", category);
	}
	
	public String[] getCategoryChoices() {
		return (String[]) session.getAttribute("categoryChoices");
	}

	public void setCategoryChoices(String category1, String category2) {
		session.setAttribute("categoryChoices", new String[] { category1, category2 });
	}

	public Integer[] getPositionChoices() {
		return (Integer[]) session.getAttribute("positionChoices");
	}

	public void setPositionChoices(int position1, int position2) {
		session.setAttribute("positionChoices", new Integer[] { position1, position2 });
	}

	public String getQuestion() {
		return (String) session.getAttribute("question");
	}
	
	public void setQuestion(String question) {
		session.setAttribute("question", question);
	}
	
	public String getResponse() {
		return (String) session.getAttribute("response");
	}
	
	public void setResponse(String question) {
		session.setAttribute("response", question);
	}

	public int getTurnNumber() {
		return (int) session.getAttribute("actualTurn");
	}

	public void setTurnNumber(int turn) {
		session.setAttribute("actualTurn", turn);
	}

	public int nextTurn() {
		int turn = getTurnNumber() + 1;
		setTurnNumber(turn);
		return turn;
	}

	public static void init(HttpSession session) {
		session.setAttribute("step", GameStep.PLAYER_COUNT);
		session.setAttribute("playerCount", 0);
		session.setAttribute("playersInfoArray", null);
		session.setAttribute("currentPlayer", 0);
		session.setAttribute("categoryChoices", null);
		session.setAttribute("category", "");
		session.setAttribute("question", "");
		session.setAttribute("response", "");
		session.setAttribute("actualTurn", 0);
	}

	public void initPlayersInfoArray(int playerCount) {
		Player[] playersInfo = new Player[playerCount];

		for (int i = 0; i < playerCount; ++i) {
			playersInfo[i] = new Player();
		}

		session.setAttribute("playersInfoArray", playersInfo);
	}
}

