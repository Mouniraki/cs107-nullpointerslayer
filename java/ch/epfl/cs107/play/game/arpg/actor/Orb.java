package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class Orb extends AreaEntity {

	private final String orbSprite = "zelda/orb";
	private Sprite[][] orb;
	private final int ANIMATION_DURATION = 4;
	private Animation[] orbAnim;
	
	/**
	 * Default Orb constructor.
	 * @param area (Area) : the area where it will be registered.
	 * @param orientation (Orientation) : the orientation of the Orb.
	 * @param position (DiscreteCoordinates) : the position of the Orb on the grid.
	 */
	public Orb(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		orb = RPGSprite.extractSprites(orbSprite, 6, 1, 1, this, 32, 32, new Orientation[]
				{Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
		orbAnim = RPGSprite.createAnimations(ANIMATION_DURATION, orb, true);
	}
	
	// Orb extends AreaEntity
	@Override
	public void update(float deltaTime) {
		orbAnim[getOrientation().ordinal()].update(deltaTime);
	}
	
	@Override
	public void draw(Canvas canvas) {
		orbAnim[getOrientation().ordinal()].draw(canvas);
	}
	
	@Override
    public boolean takeCellSpace() {
        return false;
    }

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
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
}
