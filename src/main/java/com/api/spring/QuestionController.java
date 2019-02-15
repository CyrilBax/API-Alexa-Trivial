package com.api.spring;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.game.model.Game;
import com.game.model.GameSession;


@RestController
public class QuestionController 
{
	@Autowired
	HttpSession session;
	
	@RequestMapping("/questions")
    public String getQuestions() {
		GameSession gs = new GameSession(session);
		return Game.getQuestion(gs, gs.getCategory());
    }
	
	@RequestMapping("/reponse")
    public String getResponse() {
        return new GameSession(session).getResponse(); 
    }
	
	//Vérifier questions
	@RequestMapping("/check")
	public String checkQuestion(@RequestParam(name="reponse", required=true) String reponse) {
		GameSession gs = new GameSession(session);
		int currentPlayerId = gs.getCurrentPlayerId();
		
		if (reponse.compareTo(gs.getResponse().toLowerCase()) == 0) {
			gs.incrementPlayerPoints(currentPlayerId);			
			return "Bonne réponse. Vous avez actuellement "
					+ gs.getPlayerPoints(currentPlayerId) + " points. ";
		}
		
		return "Mauvaise réponse. La bonne réponse est \""
			+ gs.getResponse()
			+ "\". Vous avez actuellement "
			+ gs.getPlayerPoints(currentPlayerId)
			+ " points. ";
	}
}
