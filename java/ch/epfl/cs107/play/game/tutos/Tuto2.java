package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.tutos.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutos.area.tuto2.*;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class Tuto2 extends AreaGame{
	private final float scaleFactor = 13.f;
	private GhostPlayer player;
	private void createAreas() {
		addArea(new Village());
		addArea(new Ferme());
	}
	
	public String getTitle() {
		return "Tuto2";
	}
	
	public boolean begin(Window window, FileSystem filesystem) {
		if(super.begin(window, filesystem)) {
			createAreas();
			setCurrentArea("zelda/Ferme", true);
			player = new GhostPlayer(getCurrentArea(), Orientation.DOWN, new DiscreteCoordinates(2, 10), "ghost.1");
			getCurrentArea().registerActor(player);
			getCurrentArea().setViewCandidate(player);
			return true;
		}
		else return false;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		player.update(deltaTime);
		if(player.isPassingADoor()) {
			switchArea();
			player.resetDoorState();
		}
		
	}
	
	public void end() {
	}
	
	void switchArea() {
		if(getCurrentArea().getTitle().equals("zelda/Village")) {
			player.leaveArea();
			Area currentArea = setCurrentArea("zelda/Ferme", false);
			player.enterArea(currentArea, new DiscreteCoordinates(2, 10));
			player.strengthen();
		}
		else if(getCurrentArea().getTitle().equals("zelda/Ferme")) {
			player.leaveArea();
			Area currentArea = setCurrentArea("zelda/Village", false);
			player.enterArea(currentArea, new DiscreteCoordinates(5, 15));
			player.strengthen();
		}
	}
}
