package com.api.spring;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.game.model.Game;
import com.game.model.GameSession;
import com.game.model.GameSession.GameStep;
import com.game.model.Player;

@RestController
public class GameController {
	
	@Autowired 
	HttpSession session;
	
	@RequestMapping("/setPlayerCount")
	public String setPlayerCount(@RequestParam(name="count",required=true) int count) {
		GameSession gs = new GameSession(session);
		gs.setPlayerCount(count);
		gs.initPlayersInfoArray(count);

		if (count == 1) {
			gs.setStep(GameStep.WAIT_TO_START);
		} else {
			gs.incrementStep();
		}

		return Game.getNextSpeechText(gs);
	}
	
	@RequestMapping("/setPlayerName")
	public String setPlayerName(@RequestParam(name="name",required=true) String name) {
		GameSession gs = new GameSession(session);
		
		int currentPlayerId = gs.getCurrentPlayerId();
		gs.setPlayerName(currentPlayerId, name);
		gs.incrementStep();
		return Game.getNextSpeechText(gs);
	}
	
	@RequestMapping("/setPlayerAge")
	public String setPlayerAge(@RequestParam(name="age",required=true) int age) {
		GameSession gs = new GameSession(session);
		int currentPlayerId = gs.getCurrentPlayerId();

		gs.setPlayerAge(currentPlayerId, age);
		currentPlayerId++;

		if (currentPlayerId == gs.getPlayerCount()) {
			ArrayList<Player> playersInfo = gs.getPlayersInfoArray();
			playersInfo.sort((e1, e2) -> { return e1.age - e2.age; });
			gs.setPlayersInfoArray(playersInfo);
			gs.setCurrentPlayerId(0);
			gs.incrementStep();
		}  else {
			gs.setCurrentPlayerId(currentPlayerId);
			gs.setStep(GameStep.PLAYER_NAME);
		}

		return Game.getNextSpeechText(gs);
	}
	
	@RequestMapping("/init")
	public String init() {
		GameSession.init(session);
		return "Bienvenue sur une partie de Trivial Pursuit. Combien y a t-il de joueur?";
	}
	
	@RequestMapping("/gameStep")
	public String gameStep() {
		return ((GameStep) session.getAttribute("step")).toString();
	}
	
	@RequestMapping("/mostValuablePlayer")
	public String getMVP() {
		GameSession gs = new GameSession(session);
		return gs.getPlayersInfoArray()
				.stream()
				.max((e1, e2) -> { return e1.points - e2.points; })
				.get().getName();
	}
}
