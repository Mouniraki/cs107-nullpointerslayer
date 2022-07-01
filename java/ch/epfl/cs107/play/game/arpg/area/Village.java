package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Village extends ARPGArea{
	@Override
	public String getTitle() {
		return "zelda/Village";
	}
	

	protected void createArea() {

		// Registering the different doors of the area
		for (int i = 4; i <= 5; i++)
			registerActor(new Door("zelda/Ferme", new DiscreteCoordinates(i, 1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(i, 19)));
		for (int i = 13; i <= 15; i++)
			registerActor(new Door("zelda/Ferme", new DiscreteCoordinates(i, 1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(i, 19)));
		for (int i = 9; i <= 10; i++)
			registerActor(new Door("zelda/Route", new DiscreteCoordinates(i, 1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(i + 20, 19)));

		// Registering LogMonsters
		registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(3, 13)));
		registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(12, 4)));
		registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(17, 6)));
		registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(28, 14)));

		// Registering the background and foreground of the area
		registerActor(new Background(this));
		registerActor(new Foreground(this));
	}
}
