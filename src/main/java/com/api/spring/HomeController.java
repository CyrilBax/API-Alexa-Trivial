package com.api.spring;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.game.model.Game;
import com.game.model.GameSession;



@RestController
public class HomeController {

	@Autowired
	HttpSession session;
	
	 @RequestMapping("/")
	    public String index() {
	        return "Bien le bonsoir"; 
	    }
	 
	 /*
	 @RequestMapping("/setplayercount")
	 public String setplayercount(@RequestParam(name="count",required=true) int count)
	 {
		 GameSession session=new GameSession(this.session);
		 session.setPlayerCount(count);
			session.initPlayersInfoArray(count);

			if (count == 1) {
				session.setStep(GameStep.WAIT_TO_START);
			} else {
				session.incrementStep();
			}
			
			return Game.getNextSpeechText(session);
	 }
	 */
}
