package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.*;

public class LogMonster extends Monster {

    private final ARPGLogMonsterHandler handler = new ARPGLogMonsterHandler();
    private final int MIN_SLEEPING_DURATION = 18;
    private final int MAX_SLEEPING_DURATION = 22;
    private final int MAX_INACTIVATION_DURATION = 24;
    // Animation duration in frame number
    private final int ANIMATION_DURATION = 6;

    private int currentState;
    private boolean isInactive;
    private int timeInactive;

    // Variables for LogMonster's graphics
    private final String spriteName = "zelda/logMonster";
    private Sprite[][] graphics;
    private Animation[] animation;
    // Variables for LogMonster's sleeping graphics
    private final String sleepingSpriteName = "zelda/logMonster.sleeping";
    private Sprite[][] sleepingGraphics;
    private Animation[] sleepingAnimation;
    // Variables for LogMonster's waking up graphics
    private final String wakingUpSpriteName = "zelda/logMonster.wakingUp";
    private Sprite[][] wakingUpGraphics;
    private Animation[] wakingUpAnimation;



    // Defining the possible states for LogMonster
    private final String[] STATES = new String[]
            {"IDLE", "ATTACKING", "FALLING_ASLEEP", "SLEEPING", "WAKING_UP"};

    /**
     * LogMonster constructor
     *
     * @param owner (Area) : Owner area. Not null
     * @param orientation (Orientation) : Initial orientation of the entity in the Area. Not null
     * @param coordinates (DiscreteCoordinates) : Initial position of the entity in the Area. Not null
     */
    public LogMonster(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        initEnergies(10);
        currentState = 0; // Idle at construction
        timeInactive = 0;
        isInactive = false;
        // Idle graphics
        graphics = RPGSprite.extractSprites(spriteName, 4, 2, 2, this, 32, 32, new Orientation[]
                { Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
        animation = RPGSprite.createAnimations(ANIMATION_DURATION, graphics);
        // Sleeping graphics
        sleepingGraphics = new Sprite[4][4];
        for (int i = 0; i < sleepingGraphics[0].length; i++) {
            sleepingGraphics[0][i] = new RPGSprite(sleepingSpriteName, 2, 2, this, new RegionOfInterest(0, 32 * i, 32, 32));
        }
        sleepingAnimation = RPGSprite.createAnimations(ANIMATION_DURATION, sleepingGraphics, true);
        // Waking up graphics
        wakingUpGraphics = new Sprite[4][3];
        for (int i = 0; i < wakingUpGraphics[0].length; i++) {
            wakingUpGraphics[0][i] = new RPGSprite(wakingUpSpriteName, 2, 2, this, new RegionOfInterest(0, 32 * i, 32, 32));
        }
        wakingUpAnimation = RPGSprite.createAnimations(ANIMATION_DURATION, wakingUpGraphics, false);
        // Log Monster vulnerabilities
        addVulnerability(DamageType.PHYSICAL);
        addVulnerability(DamageType.FIRE);
        resetMotion();
    }

    @Override
    public CollectableAreaEntity itemToDrop() {
        // Drops a coin at death
        return new Coin(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
    }

    /**
     * Log Monster experiences moments of inaction during
     * which it doesn't do anything at all
     */
    private void inactivates() {
        if (!isInactive) isInactive = true;
        ++timeInactive;
        if ((currentState == 3 && timeInactive >= (MIN_SLEEPING_DURATION + new Random().nextFloat() * (MAX_SLEEPING_DURATION - MIN_SLEEPING_DURATION)))
                || (currentState != 3 && timeInactive >= MAX_INACTIVATION_DURATION))
        {
            isInactive = false;
            timeInactive = 0;
        }
    }

    @Override
    public void update(float deltaTime) {
        // If still inactive, updates inactive state
        if (isInactive) {
            inactivates();
            if (currentState == 3) sleepingAnimation[0].update(deltaTime);
        }
        else {
            switch (currentState) {
                case 0:
                    // Moves randomly if not already
                    if (!isDisplacementOccurs()) {
                        moveRandom();
                        // Has 1 out of 4 chances to inactivate
                        if (RandomGenerator.getInstance().nextInt(4) == 1)
                            inactivates();
                    }
                    if (isDisplacementOccurs()) {
                        animation[getOrientation().ordinal()].update(deltaTime);

                    }
                    else {
                        animation[getOrientation().ordinal()].reset();
                    }
                    break;
                case 1:
                    // Keeps moving to its target
                    move(ANIMATION_DURATION - 1);
                    if (isDisplacementOccurs()) {
                        animation[getOrientation().ordinal()].update(deltaTime);
                    }
                    // Inactivates if it hits its target (or a non walkable cell)
                    else if (!isDisplacementOccurs()) ++currentState;
                    break;
                case 2:
                    inactivates();
                    ++currentState;
                    break;
                case 3:
                    sleepingAnimation[0].reset();
                    ++currentState;
                    break;
                case 4:
                    wakingUpAnimation[0].update(deltaTime);
                    if (wakingUpAnimation[0].isCompleted()) {
                        currentState = 0;
                        // Does a half turn after waking up
                        orientate(Orientation.values()[(getOrientation().ordinal() + 2) % 4]);
                        wakingUpAnimation[0].reset();
                    }
                    break;
            }
        }
        super.update(deltaTime);
    }

    /**
     * Randomly chooses to make a move
     * in a random orientation
     */
    private void moveRandom() {
        if (RandomGenerator.getInstance().nextInt(11) <= 6) {
            orientate(Orientation.values()[(getOrientation().ordinal() + RandomGenerator.getInstance().nextInt(3)) % 4]);
            move(ANIMATION_DURATION * 2);
        }
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        if (currentState == 1) return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
        else {
            List<DiscreteCoordinates> fieldOfViewCells = new ArrayList<>();
            fieldOfViewCells.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
            for (int i = 0; i < 7; i++) {
                fieldOfViewCells.add((fieldOfViewCells.get(i).jump(getOrientation().toVector())));
            }
            return fieldOfViewCells;
        }
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return (currentState == 0 || currentState == 1);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    @Override
    public void interactWith(Interactable other) { other.acceptInteraction(handler); }

    @Override
    public void draw(Canvas canvas) {
        // Draws the animation only half the time if immuneFrames is more than 0 (Simulates blinking)
        if ((getImmuneFrames() > 0 && (getImmuneFrames() % 2 == 0)) || (getImmuneFrames() == 0)) {
            if (!getIsDying()) {
                switch (currentState) {
                    case 0:
                    case 1:
                        animation[getOrientation().ordinal()].draw(canvas);
                        break;
                    case 2:
                    case 3:
                        sleepingAnimation[0].draw(canvas);
                        break;
                    case 4:
                        wakingUpAnimation[0].draw(canvas);
                }
            }
        }
        super.draw(canvas);
    }

    private class ARPGLogMonsterHandler implements ARPGInteractionVisitor {

        public void interactWith(Player player) {
            if (currentState == 1) ((ARPGPlayer) player).decreaseEnergy(2f);
            if (currentState == 0) currentState = 1; // Switches to ATTACKING
        }
    }
}
