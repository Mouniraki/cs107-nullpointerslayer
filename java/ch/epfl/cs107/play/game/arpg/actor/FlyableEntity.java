package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;

public interface FlyableEntity extends Interactable {
    /**
     * FlyableEntity is a specific concept of an interactable.
     * Every flying entity can fly by default.
     * @return (boolean)
     */
    default boolean canFly() { return true; }
}
