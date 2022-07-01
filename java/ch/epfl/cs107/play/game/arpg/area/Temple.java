package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Staff;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Temple extends ARPGArea
{
    @Override
    public String getTitle() { return "zelda/Temple"; }

    protected void createArea() {

        // Registering the different doors of the area
    	for (int i = 17; i <= 18; i++) registerActor(new Door("zelda/RouteTemple", new DiscreteCoordinates(5, 5), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(4, 0)));
        registerActor(new Staff(this, Orientation.UP, new DiscreteCoordinates(3, 3)));

        // Registering the background and foreground of the area
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
