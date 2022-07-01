package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Sword extends AreaEntity implements FlyableEntity, Interactor {

    private final int ATTACK_LENGTH = 3;
    private final ARPGSwordHandler handler = new ARPGSwordHandler();
    private int lifeTime;

    /**
     * Default Sword constructor.
     * @param area (Area) : the area where it will be displayed.
     * @param orientation (Orientation) : the orientation of the sword.
     * @param position (DiscreteCoordinates) : the position of the sword on the grid.
     */
    public Sword(Area area, Orientation orientation, DiscreteCoordinates position) {

        super(area, orientation, position);
        lifeTime = 0;
    }

    // Sword extends AreaEntity implements FlyableEntity, Interactor
    @Override
    public void update(float deltaTime) {

        if (lifeTime == ATTACK_LENGTH) getOwnerArea().unregisterActor(this);
        ++lifeTime;
    }

    @Override
    public void draw(Canvas canvas) {}

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    // Specific handler of interactions for Sword
    private class ARPGSwordHandler implements ARPGInteractionVisitor {

    	// Specific interaction with FireSpell entities
        @Override
        public void interactWith(FireSpell fireSpell) {
            fireSpell.stopFire();
        }

        // Specific interaction with Grass entities
        @Override
        public void interactWith(Grass grass) {
            grass.cut();
        }

        // Specific interaction with Monster entities
        @Override
        public void interactWith(Monster monster) {
            for (DamageType type : monster.getVulnerabilities()) {
                if (type == DamageType.PHYSICAL) {
                    monster.decreaseEnergy(2f);
                }
            }
        }
    }
}
