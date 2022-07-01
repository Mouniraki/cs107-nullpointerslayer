package ch.epfl.cs107.play.game.tutos;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.tuto1.*;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class Tuto1 extends AreaGame{
	private final float delta = 0.05f;
	private SimpleGhost player;
	
	private void createAreas() {
		addArea(new Village());
		addArea(new Ferme());
	}
	
	public String getTitle() {
		return "Tuto1";
	}
	
	public boolean begin(Window window, FileSystem filesystem) {
		if(super.begin(window, filesystem)) {
			player = new SimpleGhost(new Vector(18, 7), "ghost.1");
			createAreas();
			setCurrentArea("zelda/Village", true);
			getCurrentArea().registerActor(player);
			getCurrentArea().setViewCandidate(player);
			switchArea();
			return true;
		}
		else return false;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		Keyboard keyboard = getWindow().getKeyboard();
		Button keyUP = keyboard.get(Keyboard.UP);
		Button keyDOWN = keyboard.get(Keyboard.DOWN);
		Button keyLEFT = keyboard.get(Keyboard.LEFT);
		Button keyRIGHT = keyboard.get(Keyboard.RIGHT);
		if(keyUP.isDown()) {
			player.moveUp(delta);
		}
		if(keyDOWN.isDown()) {
			player.moveDown(delta);
		}
		if(keyLEFT.isDown()) {
			player.moveLeft(delta);
		}
		if(keyRIGHT.isDown()) {
			player.moveRight(delta);
		}
		if(player.isWeak()) {
			switchArea();
		}
		
	}
	
	public void end() {
	}
	
	void switchArea() {
		if(getCurrentArea().getTitle().equals("zelda/Village")) {
			getCurrentArea().unregisterActor(player);
			setCurrentArea("zelda/Ferme", false);
			getCurrentArea().registerActor(player);
			getCurrentArea().setViewCandidate(player);
			player.strengthen();
		}
		else if(getCurrentArea().getTitle().equals("zelda/Ferme")) {
			getCurrentArea().unregisterActor(player);
			setCurrentArea("zelda/Village", false);
			getCurrentArea().registerActor(player);
			getCurrentArea().setViewCandidate(player);
			player.strengthen();
		}
	}
}
