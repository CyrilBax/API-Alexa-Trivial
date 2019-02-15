package com.api.spring;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.game.model.Game;
import com.game.model.GameSession;
import com.game.model.GameSession.GameStep;

@RestController
public class DiceController {
	public static String plateau[]=
		{
				"Sports et loisirs",
			    "Histoire",
		        "Sciences et Nature",
		        "Arts et litterature",
		        "Geographie",
		        "Divertissement",
		        "Geographie",
		        "Histoire",
		        "Sciences et Nature",
		        "Arts et litterature",
		        "Sciences et Nature",
		        "Arts et litterature",
		        "Geographie",
		        "Divertissement",
		        "Sports et loisirs",
		        "Histoire",
		        "Sports et loisirs",
		        "Arts et litterature",
		        "Sciences et Nature",
		        "Divertissement",
		        "Geographie",
		        "Sports et loisirs",
		        "Geographie",
		        "Arts et litterature",
		        "Sports et loisirs",
		        "Histoire",
		        "Geographie",
		        "Sciences et Nature",
		        "Arts et litterature",
		        "Sciences et Nature",
		        "Histoire",
		        "Divertissement",
		        "Geographie",
		        "Histoire",
		        "Sciences et Nature",
		        "Divertissement",
		        "Arts et litterature",
		        "Histoire",
		        "Sports et loisirs",
		        "Geographie",
		        "Divertissement",
		        "Sciences et Nature"
		};
	
	@Autowired
	HttpSession session;
	
	//Lance le dé et propose au joueur deux catégories
	@RequestMapping("/de")
	public String launchDice() {
		GameSession gs = new GameSession(session);
		int playerCount = gs.getPlayerCount();

		int currentPlayerId;
		if (gs.getStep() == GameStep.WAIT_TO_START) {
			currentPlayerId = 0;
			gs.incrementStep();
		} else {
			currentPlayerId = gs.getCurrentPlayerId();
			if (gs.getPlayerPoints(currentPlayerId) == Game.MAX_POINTS) {
				return Game.gameOver(gs, playerCount);
	        }

			currentPlayerId++;
			if (currentPlayerId >= playerCount) {
				currentPlayerId = 0;
			}
		}

		gs.setStep(GameStep.SELECT_CATEGORY);
		gs.setCurrentPlayerId(currentPlayerId);

		//Récupération de la position du joueur actuel
		int position = gs.getPlayerPosition(currentPlayerId);
		
		int resultatDe = (int) (1 + (Math.random() * 6));
		int indexCategorie1 = 0;
		int indexCategorie2 = 0;
		
		if(position + resultatDe >= plateau.length) {
			indexCategorie1 = resultatDe - plateau.length + position;
		} else {
			indexCategorie1 = position + resultatDe;
		}
		
		
		if(position - resultatDe < 0) {
			indexCategorie2 = plateau.length - resultatDe + position;
		} else {
			indexCategorie2 = position - resultatDe;
		}

		String c1 = plateau[indexCategorie1];
		String c2 = plateau[indexCategorie2];

		if (c1.equals(c2)) {
			int i = (int) (Math.random() * 2);
			if (i == 0) {
				gs.setPlayerPosition(currentPlayerId, (position + resultatDe) % plateau.length);
			} else {
				gs.setPlayerPosition(currentPlayerId, (position - resultatDe) % plateau.length);
			}

			return "Au tour de " + gs.getPlayerName(currentPlayerId)
				+ ". Vous avez lancé les dés. Vous avez fais "
				+ resultatDe
				+ ". Vous êtes tombé sur la catégorie "
				+ c1
				+ ". "
				+ Game.getQuestion(new GameSession(session), c1);
		}

		gs.setCategoryChoices(c1, c2);
		gs.setPositionChoices(
			(position + resultatDe) % plateau.length,
			(position - resultatDe) % plateau.length
		);

		return "Au tour de " + gs.getPlayerName(currentPlayerId)
			+ ". Vous avez lancé les dés. Vous avez fais "
			+ resultatDe
			+ ". Vous avez le choix entre la catégorie "
			+ c1
			+ " ou la catégorie "
			+ c2
			+ ". Que souhaitez vous choisir?";
	}
	
	@RequestMapping("/getCategories")
	public String getCategories() {
		String[] categoryChoices = new GameSession(session).getCategoryChoices();
		return "Vous avez le choix entre la catégorie "
				+ categoryChoices[0]
						+ " ou la catégorie "
				+ categoryChoices[1]
				+ ". Que souhaitez vous choisir?";
	}

	@RequestMapping("/setCategory")
	public String setCategory(@RequestParam(name="category") String category) {
		GameSession gs = new GameSession(session);
		String[] categoryChoices = gs.getCategoryChoices();
		Integer[] positionChoices = gs.getPositionChoices();
		
		if (categoryChoices[0].equalsIgnoreCase(category)) {
			gs.setPlayerPosition(gs.getCurrentPlayerId(), positionChoices[0]);
			return "Vous avez choisis la catégorie " + categoryChoices[0]
					+ ". " + Game.getQuestion(gs, categoryChoices[0]);
		} else if (categoryChoices[1].equalsIgnoreCase(category)) {
			gs.setPlayerPosition(gs.getCurrentPlayerId(), positionChoices[1]);
			return "Vous avez choisis la catégorie " + categoryChoices[1]
					+ ". " + Game.getQuestion(gs, categoryChoices[1]);
		}

		return "Vous n'avez le choix que entre la catégorie " + categoryChoices[0]
				+ " ou la catégorie " + categoryChoices[1];
	}
}
