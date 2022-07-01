package ch.epfl.cs107.play.game.arpg.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FireSpell extends AreaEntity implements Interactor {

    private final ARPGFireSpellHandler handler = new ARPGFireSpellHandler();

    private int lifeTime;
    private int firePower;
    private int propagationTimer;
    private int lifeCounter;

    private final String spriteName = "zelda/fire";
    private Sprite[][] graphics;
    private Animation[] animation;

    private final int MIN_LIFE_TIME = 120;
    private final int MAX_LIFE_TIME = 240;
    private final int PROPAGATION_TIME_FIRE = 42;
    private final int ANIMATION_DURATION = 6;

    /**
     * FireSpell constructor
     *
     * @param area (Area) : Owner area. Not null
     * @param orientation (Orientation) : Initial orientation of the entity in the Area. Not null
     * @param position (DiscreteCoordinates) : Initial position of the entity in the Area. Not null
     * @param power (int) : Defines how many times the FireSpell can propagate
     */
    public FireSpell(Area area, Orientation orientation, DiscreteCoordinates position, int power) {
        super(area, orientation, position);
        // FIRE SPELL LIFETIME IS A RANDOM VALUE BETWEEN TWO CONSTANTS
        lifeTime = MIN_LIFE_TIME + new Random().nextInt(MAX_LIFE_TIME - MIN_LIFE_TIME + 1);
        firePower = power;
        propagationTimer = 0;
        // FIRE SPELL GRAPHICS
        graphics = new Sprite[4][7];
        for (int i = 0; i < graphics[0].length; i++) {
            graphics[0][i] = new RPGSprite(spriteName, 1, 1, this, new RegionOfInterest(i * 16, 0, 16, 16));
        }
        animation = RPGSprite.createAnimations(ANIMATION_DURATION, graphics, true);
    }

    @Override
    public void update(float deltaTime) {
        animation[0].update(deltaTime);
        if ((propagationTimer == PROPAGATION_TIME_FIRE) && (firePower > 0)) {
            if (getOwnerArea().canEnterAreaCells(this, getFieldOfViewCells())) {
                getOwnerArea().registerActor(new FireSpell(getOwnerArea(), getOrientation(), getFieldOfViewCells().get(0), firePower - 1));
            }
        }
        if (lifeCounter == lifeTime) stopFire();
        ++propagationTimer;
        ++lifeCounter;
        super.update(deltaTime);
    }

    /**
     * Unregisters the actor when lifeTime is reached
     * or an interaction asks for it
     */
    public void stopFire() {
        getOwnerArea().unregisterActor(this);
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
    public boolean takeCellSpace() {
        return false;
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
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor) v).interactWith(this);
    }

    @Override
    public void draw(Canvas canvas) {
        animation[0].draw(canvas);
    }

    private class ARPGFireSpellHandler implements ARPGInteractionVisitor {
        @Override
        public void interactWith(Player player) { ((ARPGPlayer) player).decreaseEnergy(1f); }
        @Override
        public void interactWith(Monster monster) {
            for (DamageType vulnerability : monster.getVulnerabilities()) {
                if (vulnerability == DamageType.FIRE) monster.decreaseEnergy(1f);
            }
        }
        @Override
        public void interactWith(Grass grass) {
            grass.cut();
        }
        @Override
        public void interactWith(Bomb bomb) {
            bomb.explodes();
        }
    }
}
