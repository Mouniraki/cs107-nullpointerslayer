package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.Bomb;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Ferme extends ARPGArea
{
	@Override
	public String getTitle() {
		return "zelda/Ferme";
	}

	protected void createArea() {

		// Registering the different doors of the area
		for (int i = 15; i <= 16; i++) registerActor(new Door("zelda/Route", new DiscreteCoordinates(1, i), Logic.TRUE, this, Orientation.RIGHT, new DiscreteCoordinates(19, i)));
		for (int i = 4; i <= 5; i++) registerActor(new Door("zelda/Village", new DiscreteCoordinates(i, 18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(i, 0)));
		for (int i = 13; i <= 15; i++) registerActor(new Door("zelda/Village", new DiscreteCoordinates(i, 18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(i, 0)));

		// Registering the background and foreground of the area
        registerActor(new Background(this));
        registerActor(new Foreground(this));
	}
}
