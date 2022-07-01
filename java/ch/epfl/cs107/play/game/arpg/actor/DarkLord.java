package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DarkLord extends Monster {

    private final ARPGDarkLordHandler handler = new ARPGDarkLordHandler();
    private final int FOV_RADIUS = 3;
    private final int TELEPORTATION_RADIUS = 5;
    private final int MIN_SPELL_WAIT_DURATION = 84;
    private final int MAX_SPELL_WAIT_DURATION = 126;
    private final int MAX_INACTIVATION_DURATION = 24;
    // Animation duration in frame number
    private final int ANIMATION_DURATION = 6;

    private int currentState;
    private int spellWaitDuration;
    private int coolDownCounter;
    private boolean orientationFound;
    private int randomOrientationOrdinal;
    private boolean isInactive;
    private int timeInactive;
    private boolean destinationFound;

    private final String spriteName = "zelda/darkLord";
    private Sprite[][] graphics;
    private Animation[] animation;

    private final String spellCastSpriteName = "zelda/darkLord.spell";
    private Sprite[][] spellCastGraphics;
    private Animation[] spellCastAnimation;


    // Defining the possible states for DarkLord
    private final String[] STATES = new String[]
            {"IDLE", "ATTACKING", "CASTING_SPAWNING_SPELL", "CASTING_TELEPORT_SPELL", "TELEPORTING"};

    /**
     * DarkLord constructor
     *
     * @param owner (Area) : Owner area. Not null
     * @param orientation (Orientation) : Initial orientation of the entity in the Area. Not null
     * @param coordinates (DiscreteCoordinates) : Initial position of the entity in the Area. Not null
     */
    public DarkLord(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {
        super(owner, orientation, coordinates);
        initEnergies(15);
        currentState = 0; // IDLE AT CONSTRUCTION
        timeInactive = 0;
        coolDownCounter = 0;
        randomOrientationOrdinal = 0;
        isInactive = false;
        orientationFound = false;
        destinationFound = false;
        // Spell wait duration will be re-defined between these two constants each time a spell is cast
        spellWaitDuration = new Random().nextInt((MAX_SPELL_WAIT_DURATION - MIN_SPELL_WAIT_DURATION) + 1) + MIN_SPELL_WAIT_DURATION;
        // Standard graphics
        graphics = RPGSprite.extractSprites(spriteName, 3, 2, 2, this, 32, 32, new Orientation[]
                { Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        animation = RPGSprite.createAnimations(ANIMATION_DURATION / 2, graphics);
        // Spell cast graphics
        spellCastGraphics = RPGSprite.extractSprites(spellCastSpriteName, 3, 2, 2, this, 32, 32, new Orientation[]
                { Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        spellCastAnimation = RPGSprite.createAnimations(ANIMATION_DURATION / 2, spellCastGraphics, false);
        // Dark Lord vulnerability
        addVulnerability(DamageType.MAGIC);
    }

    @Override
    protected CollectableAreaEntity itemToDrop() {
        // Drops a CastleKey at death
        return new CastleKey(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates());
    }


    /**
     * Dark Lord experiences moments of inaction during
     * which it doesn't do anything at all
     */
    private void inactivates() {
        if (!isInactive) isInactive = true;
        ++timeInactive;
        if (timeInactive >= MAX_INACTIVATION_DURATION) {
            isInactive = false;
            timeInactive = 0;
        }
    }

    @Override
    public void update(float deltaTime) {
        /* If the counter reaches spell wait duration, Dark Lord
           tries to find an orientation that lets it summon a FireSpell,
           and switches to either "attacking" or "casting spawning spell"
         */
        if (spellWaitDuration == coolDownCounter) {
            currentState = (new Random().nextInt(5) > 2) ? 1 : 2;
            while (!orientationFound) {
                randomOrientationOrdinal = new Random().nextInt(4);
                if (getOwnerArea().canEnterAreaCells(new FireSpell(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 3),
                        Collections.singletonList(getCurrentMainCellCoordinates().jump(Orientation.values()[randomOrientationOrdinal].toVector())))) {
                    orientationFound = true;
                    if (randomOrientationOrdinal != getOrientation().ordinal()) orientate(Orientation.values()[randomOrientationOrdinal]);
                }
            }
            orientationFound = false;
            spellWaitDuration = new Random().nextInt((MAX_SPELL_WAIT_DURATION - MIN_SPELL_WAIT_DURATION) + 1) + MIN_SPELL_WAIT_DURATION;
            coolDownCounter = 0;
        }
        if (isInactive) {
            // If still inactive, updates inactive state
            inactivates();
        }
        else {
            switch (currentState) {
                case 0:
                    if (!isDisplacementOccurs()) {
                        moveRandom();
                        if (RandomGenerator.getInstance().nextInt(4) == 1)
                            inactivates();
                    }
                    if (isDisplacementOccurs()) {
                        animation[getOrientation().ordinal()].update(deltaTime);

                    } else {
                        animation[getOrientation().ordinal()].reset();
                    }
                    break;
                case 1:
                    spellCastAnimation[getOrientation().ordinal()].update(deltaTime);
                    if (spellCastAnimation[getOrientation().ordinal()].isCompleted()) {
                        currentState = 0;
                        getOwnerArea().registerActor(new FireSpell(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()), 3));
                    }
                    break;
                case 2:
                    spellCastAnimation[getOrientation().ordinal()].update(deltaTime);
                    if (spellCastAnimation[getOrientation().ordinal()].isCompleted()) {
                        currentState = 0;
                        getOwnerArea().registerActor(new FlameSkull(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector())));
                    }
                    break;
                case 3:
                    spellCastAnimation[getOrientation().ordinal()].update(deltaTime);
                    if (spellCastAnimation[getOrientation().ordinal()].isCompleted()) {
                        spellCastAnimation[getOrientation().ordinal()].reset();
                        ++currentState;
                    }
                    break;
                case 4:
                    animation[getOrientation().ordinal()].update(deltaTime);
                    int x = 0;
                    int y = 0;
                    while (!destinationFound) {
                        // Computes random coordinates for teleportation until an adequate one is found
                        x = (-TELEPORTATION_RADIUS) + new Random().nextInt((2 * TELEPORTATION_RADIUS) + 1) + getCurrentMainCellCoordinates().x;
                        y = (-TELEPORTATION_RADIUS) + new Random().nextInt((2 * TELEPORTATION_RADIUS) + 1) + getCurrentMainCellCoordinates().y;
                        if (x >= 0 && y >= 0 && x < getOwnerArea().getWidth() && y < getOwnerArea().getHeight()) {
                            if (getOwnerArea().canEnterAreaCells(this, Collections.singletonList(new DiscreteCoordinates(x, y)))) {
                                /* These lines we believe have problems as when teleportation occurs, invisible obstacles are generated,
                                   unfortunately we couldn't figure out how to fix it */
                                getOwnerArea().leaveAreaCells(this, Collections.singletonList(getCurrentMainCellCoordinates()));
                                setCurrentPosition(new Vector(x, y));
                                getOwnerArea().enterAreaCells(this, Collections.singletonList(new DiscreteCoordinates(x, y)));
                                destinationFound = true;
                            }
                        }
                    }
                    destinationFound = false;
                    currentState = 0;
            }
        }
        ++coolDownCounter;
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
        ArrayList<DiscreteCoordinates> fieldOfViewCells = new ArrayList<>();
        for (int i = getCurrentMainCellCoordinates().x - FOV_RADIUS; i <= getCurrentMainCellCoordinates().x + FOV_RADIUS; i++) {
            for (int j = getCurrentMainCellCoordinates().y - FOV_RADIUS; j <= getCurrentMainCellCoordinates().y + FOV_RADIUS; j++) {
                fieldOfViewCells.add(new DiscreteCoordinates(i, j));
            }
        }
        return fieldOfViewCells;
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() { return ((currentState != 1 || currentState != 2) && !getIsDying()); }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    @Override
    public void draw(Canvas canvas) {
        // Draws the animation only half the time if immuneFrames is more than 0 (Simulates blinking)
        if ((getImmuneFrames() > 0 && (getImmuneFrames() % 2 == 0)) || (getImmuneFrames() == 0)) {
            if (!getIsDying()) {
                switch (currentState) {
                    case 0:
                    case 4:
                        animation[getOrientation().ordinal()].draw(canvas);
                        break;
                    case 1:
                    case 2:
                    case 3:
                        spellCastAnimation[getOrientation().ordinal()].draw(canvas);
                }
            }
        }
        super.draw(canvas);
    }

    private class ARPGDarkLordHandler implements ARPGInteractionVisitor {
        @Override
        public void interactWith(Player player) {
            if (currentState == 0) currentState = 3;
        }
    }
}
