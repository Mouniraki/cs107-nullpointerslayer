package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class RouteTemple extends ARPGArea
{
    @Override
    public String getTitle() { return "zelda/RouteTemple"; }

    protected void createArea() {

        // Registering the different doors of the area
    	for (int i = 17; i <= 18; i++) registerActor(new Door("zelda/Temple", new DiscreteCoordinates(4, 1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(5, 6)));
    	for (int i = 10; i <= 11; i++) registerActor(new Door("zelda/Route", new DiscreteCoordinates(18, 10), Logic.TRUE, this, Orientation.LEFT, new DiscreteCoordinates(0, 4)));
    	registerActor(new LogMonster(this, Orientation.UP, new DiscreteCoordinates(4, 4)));

    	// Registering the background and foreground of the area
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
