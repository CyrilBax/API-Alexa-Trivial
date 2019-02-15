package com.game.model;

import java.io.IOException;

import com.game.model.GameSession.GameStep;
import com.game.model.Questions.QuestionCard;


public class Game {
	public static final int MAX_POINTS = 6;

	public static String getNextSpeechText(GameSession session) {
		switch (session.getStep()) {
			case PLAYER_NAME:
				return "Indiquez le nom du joueur " + (session.getCurrentPlayerId() + 1) + ".";

			case PLAYER_AGE:
				return "Quelle à âge à le joueur" + (session.getCurrentPlayerId() + 1) + "?";

			case WAIT_TO_START:
				return "Quand vous êtes prêt dites: commencer.";

			case GAMEOVER:
				return gameOver(session, session.getPlayerCount());

			default:
				throw new IllegalStateException();
		}
	}

	public static String getQuestion(GameSession session, String categorie) {
		session.setStep(GameStep.QUESTION);

		String speechText;
		QuestionCard questionCard;

		try {
			questionCard = new Questions().getQuestion(categorie);
		} catch (IOException e) {
			return null;
		}

		session.setQuestion(questionCard.getQuestion());
		session.setResponse(questionCard.getResponse());

		if (session.getPlayerCount() == 1) {
			speechText = questionCard.getQuestion();
		} else {
			speechText = "Question pour " + session.getPlayerName(session.getCurrentPlayerId())
				+ ". " + questionCard.getQuestion();
		}

		return speechText;
	}
	
	public static String gameOver(GameSession session, int playerCount) {
		session.setStep(GameStep.GAMEOVER);

		StringBuilder speechTextStringBuilder = new StringBuilder("Partie terminé.");
		
		if (playerCount == 1) {
			speechTextStringBuilder
				.append(" Vous avez eu ")
				.append(session.getPlayerPoints(0))
				.append(" points.");
		} else {
			for (int i = 0; i < playerCount; ++i) {
				speechTextStringBuilder
					.append(" Le joueur ")
					.append(session.getPlayerName(i))
					.append(" a ")
					.append(session.getPlayerPoints(i))
					.append(" points.");
			}

			String winnerName = session.getPlayersInfoArray().stream()
				.max((e1, e2) -> { return e1.points - e2.points; })
				.get().name;
			speechTextStringBuilder.append(" Le gagnant de la partie est " + winnerName + ".");
		}

		return speechTextStringBuilder.toString();
	}
}


