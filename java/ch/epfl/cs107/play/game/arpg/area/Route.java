package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.*;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;

public class Route extends ARPGArea{
	@Override
	public String getTitle() {
		return "zelda/Route";
	}

	protected void createArea() {

		// Registering the different doors of the area
		for (int i = 15; i <= 16; i++) registerActor(new Door("zelda/Ferme", new DiscreteCoordinates(18, i), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(0, i)));
		for (int i = 9; i <= 10; i++) registerActor(new Door("zelda/Village", new DiscreteCoordinates(i + 20, 18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(i, 0)));
		for (int i = 9; i <= 10; i++) registerActor(new Door("zelda/RouteChateau", new DiscreteCoordinates(i, 1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(i, 19)));
		for (int i = 17; i <= 18; i++) registerActor(new Door("zelda/RouteTemple", new DiscreteCoordinates(1, 4), Logic.TRUE, this, Orientation.RIGHT, new DiscreteCoordinates(19, 10)));

		// Registering tiles of grass at a given location
		for (int i = 5; i <= 7; ++i) {
			for(int j = 6; j <= 11; ++j) {
				registerActor(new Grass(this, Orientation.UP, new DiscreteCoordinates(i, j)));
			}
		}
		// Registering a bomb close to the grass
		registerActor(new Bomb(this, Orientation.UP, new DiscreteCoordinates(4, 8)));

		// Registering a lonely LogMonster
		registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(12, 15)));

		// Registering an orb to shoot at with a bow
		registerActor(new Orb(this, Orientation.UP, new DiscreteCoordinates(19, 8)));

		// Registering a waterfall
		registerActor(new Waterfall(this, Orientation.DOWN, new DiscreteCoordinates(15, 3)));

		// Registering the background and foreground of the area
        registerActor(new Background(this));
        registerActor(new Foreground(this));
	}

	@Override
	public void update(float deltaTime) {

		//Press S to spawn a FlameSkull
		if (this.getKeyboard().get(Keyboard.S).isPressed()) registerActor(new FlameSkull(this, Orientation.DOWN, new DiscreteCoordinates(3, 8)));

		//Press F to spawn a FireSpell
		if (this.getKeyboard().get(Keyboard.F).isPressed()) registerActor(new FireSpell(this, Orientation.DOWN, new DiscreteCoordinates(8, 8), 10));
		super.update(deltaTime);
	}
}
