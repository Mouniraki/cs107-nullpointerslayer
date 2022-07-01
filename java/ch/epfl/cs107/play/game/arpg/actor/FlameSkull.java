package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FlameSkull extends Monster implements FlyableEntity {

    private final APRGFlameSkullHandler handler = new APRGFlameSkullHandler();
    private final float MIN_LIFE_TIME = 7f;
    private final float MAX_LIFE_TIME = 9f;
    // Animation duration in frame number
    private final int ANIMATION_DURATION = 8;

    private float lifeTime;
    private final String spriteName = "zelda/flameSkull";
    private Sprite[][] graphics;
    private Animation[] animation;


    /**
     * FlameSkull constructor
     *
     * @param owner (Area) : Owner area. Not null
     * @param orientation (Orientation) : Initial orientation of the entity in the Area. Not null
     * @param coordinates (DiscreteCoordinates) : Initial position of the entity in the Area. Not null
     */
    public FlameSkull(Area owner, Orientation orientation, DiscreteCoordinates coordinates) {

        super(owner, orientation, coordinates);
        initEnergies(10);
        // Lifetime is randomly defined between two constants at construction
        lifeTime = MIN_LIFE_TIME + new Random().nextFloat() * (MAX_LIFE_TIME - MIN_LIFE_TIME);
        // Standard Flame Skull graphics
        graphics = RPGSprite.extractSprites(spriteName, 3, 2, 2, this, 32, 32, new Orientation[]
                { Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
        animation = RPGSprite.createAnimations(ANIMATION_DURATION / 2, graphics);
        // Flame Skull vulnerabilities
        addVulnerability(DamageType.PHYSICAL);
        addVulnerability(DamageType.MAGIC);
        resetMotion();
    }

    @Override
    public CollectableAreaEntity itemToDrop() {
        return null;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void update(float deltaTime) {

        if (lifeTime > 0) lifeTime -= deltaTime;
        else { dies(); }

        if (getIsDying()) deathAnimation[0].update(deltaTime);

        if (!getIsDying()) moveRandom();

        if (isDisplacementOccurs()) {
            animation[getOrientation().ordinal()].update(deltaTime);
        }
        else {
            animation[getOrientation().ordinal()].reset();
        }
        super.update(deltaTime);
    }

    /**
     * Randomly chooses to move if not already
     * with a random orientation
     */
    public void moveRandom() {
        if (RandomGenerator.getInstance().nextInt(11) <= 6) {
            if (!isDisplacementOccurs()) {
                orientate(Orientation.values()[(getOrientation().ordinal() + RandomGenerator.getInstance().nextInt(3)) % 4]);
                move(ANIMATION_DURATION);
            }
        }
    }

    @Override
    public boolean takeCellSpace() { return false; }

    @Override
    public boolean wantsViewInteraction() { return false; }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) { ((ARPGInteractionVisitor) v).interactWith(this); }

    @Override
    public void interactWith(Interactable other) { other.acceptInteraction(handler); }

    @Override
    public void draw(Canvas canvas) {
        // Draws the animation only half the time if immuneFrames is more than 0 (Simulates blinking)
        if ((getImmuneFrames() > 0 && (getImmuneFrames() % 2 == 0)) || (getImmuneFrames() == 0)) {
            if (!getIsDying()) animation[getOrientation().ordinal()].draw(canvas);
        }
        super.draw(canvas);
    }
    private class APRGFlameSkullHandler implements ARPGInteractionVisitor {
        @Override
        public void interactWith(Monster monster) {
            for (DamageType vulnerability : monster.getVulnerabilities()) {
                if (vulnerability == DamageType.FIRE) monster.decreaseEnergy(1f);
            }
        }
        @Override
        public void interactWith(Player player) { ((ARPGPlayer) player).decreaseEnergy(1f); }
        @Override
        public void interactWith(Bomb bomb) { bomb.explodes(); }
        @Override
        public void interactWith(Grass grass) { grass.cut(); }

        public void interactWith(Arrow arrow) { decreaseEnergy(10f);}
    }
}
