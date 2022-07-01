package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Monster extends MovableAreaEntity implements Interactor, Interactable {
    private final int ANIMATION_DURATION = 2;
    private final int MAX_IMMUNE_FRAMES = 16; // FRAMES OF IMMUNITY AFTER IT TAKES DAMAGES
    private float maxEnergy;
    private float energy;
    private boolean isDying;
    private int immuneFrames;
    // ArrayList instead of LinkedList because values will be more often accessed than added/removed
    private List<DamageType> vulnerabilities = new ArrayList<DamageType>();
    private final String deathName = "zelda/vanish";
    private Sprite[][] deathGraphics;
    protected Animation[] deathAnimation;

    /**
     * Monster constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Monster(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        isDying = false;
        immuneFrames = 0;
        deathGraphics = new Sprite[4][7];
        for (int i = 0; i < deathGraphics[0].length; i++) {
            deathGraphics[0][i] = new RPGSprite(deathName, 2, 2, this, new RegionOfInterest(i * 32, 0, 32, 32));
        }
        deathAnimation = RPGSprite.createAnimations(ANIMATION_DURATION, deathGraphics, false);
    }

    /**
     * @return (CollectableAreaEntity): The item to drop by the monster after death,
     *                                  null if the monster doesn't drop items
     */
    protected abstract CollectableAreaEntity itemToDrop();

    /**
     * Getter for the monster's remaining immune frames
     * @return (int)
     */
    public int getImmuneFrames() { return immuneFrames; }

    /**
     * A method that notifies update and draw of the monster's death
     * and unregisters the entity from the area after death animation
     */
    protected void dies() {

        if (!isDying) {
            isDying = true;
            resetMotion();
        }
        if (deathAnimation[0].isCompleted()) {
            getOwnerArea().unregisterActor(this);
            if (getOwnerArea().canEnterAreaCells(new Coin(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates()),
                    Collections.singletonList(getCurrentMainCellCoordinates())) && itemToDrop() != null) getOwnerArea().registerActor(itemToDrop());
        }
    }

    /**
     * Getter for the monster's death status
     * @return (boolean)
     */
    public boolean getIsDying() { return isDying; }

    /**
     * Setter for the monster's maximum health points and current health points
     */
    protected void initEnergies(float initEnergy) {
        if (initEnergy > 0) {
            maxEnergy = initEnergy;
            energy = initEnergy;
        }
    }

    protected void decreaseEnergy(float amount) {
        if (immuneFrames == 0) {
            if (amount > energy) energy = 0;
            else energy -= amount;
            immuneFrames = MAX_IMMUNE_FRAMES;
        }
    }

    protected void addVulnerability(DamageType vulnerability) {
        vulnerabilities.add(vulnerability);
    }

    /**
     * Getter for the monster's vulnerabilities
     * @return (List<DamageType>)
     */
    public List<DamageType> getVulnerabilities() { return vulnerabilities; }

    @Override
    public void update(float deltaTime) {
        if (energy <= 0) {
            dies();
            deathAnimation[0].update(deltaTime);
        }
        if (energy > maxEnergy) energy = maxEnergy;
        if (immuneFrames > 0) --immuneFrames;
        super.update(deltaTime);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return !isDying;
    }

    @Override
    public boolean wantsViewInteraction() {
        return !isDying;
    }

    @Override
    public boolean takeCellSpace() {
        return !isDying;
    }

    @Override
    public boolean isCellInteractable() {
        return !isDying;
    }

    @Override
    public boolean isViewInteractable() {
        return !isDying;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isDying) deathAnimation[0].draw(canvas);
    }
}
