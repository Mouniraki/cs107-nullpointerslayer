package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.DarkLord;
import ch.epfl.cs107.play.game.arpg.actor.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.LogMonster;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;

public class RouteChateau extends ARPGArea
{
    @Override
    public String getTitle() { return "zelda/RouteChateau"; }

    protected void createArea() {

        // Registering the different doors of the area
        for (int i = 9; i <= 10; i++) registerActor(new Door("zelda/Route", new DiscreteCoordinates(i, 18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(i, 0)));
        registerActor(new CastleDoor("zelda/Chateau", new DiscreteCoordinates(7, 1), Logic.FALSE, this, Orientation.UP, new DiscreteCoordinates(9, 13), new DiscreteCoordinates(10, 13)));

        // Registering the background and foreground of the area
        registerActor(new DarkLord(this, Orientation.DOWN, new DiscreteCoordinates(9, 12)));
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }

    @Override
    public void update(float deltaTime) {

    	//Press S to spawn a FlameSkull
        if (this.getKeyboard().get(Keyboard.S).isPressed()) registerActor(new FlameSkull(this, Orientation.DOWN, new DiscreteCoordinates(7, 5)));

        //Press L to spawn a LogMonster
        if (this.getKeyboard().get(Keyboard.L).isPressed()) registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(9, 9)));
        super.update(deltaTime);
    }
}
