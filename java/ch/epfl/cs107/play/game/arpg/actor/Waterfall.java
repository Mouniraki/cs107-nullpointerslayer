package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Collections;
import java.util.List;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Waterfall extends AreaEntity implements FlyableEntity, Interactor {

	private final String waterfallSprite = "zelda/waterfall";
	private RPGSprite[][] waterfall;
	private final int ANIMATION_DURATION = 4;
	private Animation[] waterfallAnim;
	
	/**
	 * Default Waterfall constructor.
	 * @param area (Area) : the area where it will be registered.
	 * @param orientation (Orientation) : the orientation of the Waterfall.
	 * @param position (DiscreteCoordinates) : the position of the object on the grid.
	 */
	public Waterfall(Area area, Orientation orientation, DiscreteCoordinates position) {

		super(area, orientation, position);
		waterfall = new RPGSprite[4][3];
		for(int i=0; i<waterfall[0].length; ++i) {
			waterfall[0][i] = new RPGSprite(waterfallSprite, 4.f, 4.f, this, new RegionOfInterest(i*64, 0, 64, 64));
		}
		waterfallAnim = RPGSprite.createAnimations(ANIMATION_DURATION, waterfall, true);
	}
	
	// Waterfall extends AreaEntity implements FlyableEntity
	@Override
	public void update(float deltaTime) {
		waterfallAnim[0].update(deltaTime);
	}
	
	@Override
	public void draw(Canvas canvas) {
		waterfallAnim[0].draw(canvas);
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
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return null;
	}

	@Override
	public boolean wantsCellInteraction() {
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		return false;
	}

	@Override
	public void interactWith(Interactable other) {}
}
